# eForms SDK Analyzer

## What it is

A CLI for static analysis of eForms SDK content. It loads SDK artifacts (fields, codelists, notice types, view templates, schematron files, XML examples) and runs correctness/consistency checks. Exit code 0 = no errors; 1 = errors found.

It is also used as a library by the Metadata Manager (MDM) and its CLI to validate content stored in the eForms Metadata Database (MDD) before export.

## Versioning

The analyzer version tracks the SDK version: SDK X.Y.Z is validated by analyzer X.Y.Z, even when the analyzer code itself has not changed.

## Build & test

```bash
mvn clean package                                    # build + run all tests
mvn test -Dcucumber.filter.tags="@tedefo-XXXX"       # run tests for one Jira ticket
```

Java 11+, Maven 3.8+. SNAPSHOT dependencies on `eforms-core-java` and `efx-toolkit-java` must be available in the local Maven repo (install them first if they are not published).

## Running the analyzer

```bash
java -jar target/eforms-sdk-analyzer-*-all.jar <sdk-folder>
java -jar target/eforms-sdk-analyzer-*-all.jar <sdk-folder> --skip-efx   # skip slow EFX pass
java -jar target/eforms-sdk-analyzer-*-all.jar <sdk-folder> benchmark    # schematron perf
```

Framework logs go to stderr; the summary goes to stdout. The summary and the full per-finding detail are also written to `analyzer-summary.txt` and `analyzer-report.txt`, and a full INFO log to `analyzer.log` — all in the working directory.

## Architecture

### Validators (run sequentially by `SdkAnalyzer`)

| Class | What it checks |
|-------|---------------|
| `XmlSchemaValidator` | SDK XML files validate against their XSD schemas |
| `SchematronValidator` | Schematron files compile without errors |
| `SdkValidator` | All Drools-based rules (the bulk of the analysis) |
| `EfxValidator` | EFX expressions compile correctly — both view templates and field constraints in `fields.json` (slowest pass; `--skip-efx` omits it) |

### Content source abstraction

`SdkContentSource` defines how SDK data is loaded. Two implementations exist:
- `SdkLoader` — reads from a filesystem SDK folder (`SourceKind.FILE`)
- A database-backed implementation in the MDM — reads from the MDD via JOOQ (`SourceKind.DATABASE`)

### Drools rule engine

Rules live in `.drl` files under `src/main/resources/.../drools/`:

| File | Domain |
|------|--------|
| `codelistRules.drl` | Codelists consistency |
| `fieldsAndNodesRules.drl` | Fields & XML structure nodes |
| `noticeTypeRules.drl` | Notice types & document types |
| `schematronRules.drl` | Schematron file metadata |
| `translationRules.drl` | Labels & translations |
| `viewTemplatesRules.drl` | View templates |
| `xmlNoticeRules.drl` | XML notice examples |

SDK content is loaded into Drools `DataStore` objects via `FactsLoader` → the `SdkUnit` rule unit → `RulesRunner` fires applicable rules.

### Rule applicability annotations

DRL rules support metadata annotations that control when they fire:

```drl
rule "Example"
  @sdkMajor(2)        // only fires for SDK major version 2
  @source(FILE)       // only fires when source is a filesystem export
  @problem("Human-readable problem statement for the report")
when ...
```

- `@sdkMajor(N)` or `@sdkMajor(N, M)` — restrict to specific SDK major versions. Use when the artifact being checked does not exist in all SDK versions.
- `@source(FILE)` or `@source(DATABASE)` or `@source(FILE, DATABASE)` — restrict by content source. Use `FILE` for rules that check filenames, index presence, or file format. Omit when the check applies regardless of source.
- `@problem("...")` — actionable problem statement shown in the report. All Drools rules should have one. The rule name states the desired invariant; `@problem` states the violation observed.
- Rules without annotations apply to every profile (every SDK version, every source).

### Report output

Both the console and the files open with a title banner (SDK version, analyser version, run time in UTC). `SummaryReportRenderer` groups findings by SDK section and by problem statement; this summary goes to the console and `analyzer-summary.txt`. `DetailReportRenderer` writes the full per-finding list to `analyzer-report.txt`.

## Adding a new rule

Before adding a rule, check existing DRL files for duplicates or overlapping rules that could be updated instead.

### Steps

1. **DRL rule** — Add to the appropriate `.drl` file (or create a new one if a new domain). Include `@problem("...")`. Add `@sdkMajor` / `@source` only if the check is version- or source-specific.

2. **Cucumber feature** — Create `src/test/resources/.../cucumber/tedefo-XXXX.feature` tagged `@tedefo-XXXX`. Reference the rule name(s) in the `Background` section with `Given The following rules`.

3. **Test data** — Create `src/test/resources/eforms-sdk-tests/tedefo-XXXX/valid/` and `invalid/` folders. Put minimal SDK file fragments (only the files relevant to the rule).

4. **Feature scenarios** — Write at minimum a happy-path (valid data → 0 errors) and an unhappy-path (invalid data → expected rule fires, N errors).

5. **Run** — `mvn test -Dcucumber.filter.tags="@tedefo-XXXX"` to verify.

### If a new Fact type is needed

When the existing facts do not cover the data your rule needs:
1. Create a domain class under `domain/`
2. Create a fact wrapper under `fact/` (naming: `XxxFact`)
3. Add a loader method in `FactsLoader`
4. Add a `DataStore` field in `SdkUnit`
5. Wire it in `SdkValidator.validate()` and `SdkContentSource`
6. Add a `@When` step in `SdkValidationSteps` if needed

## Branch model

Gitflow: feature branches → PR to `develop` → merge to `main` for release.

## Cross-repo compatibility with mdm-lib (avoid breaking the MDM build)

`mdm-lib` (eforms-metadata-manager) depends on this project as a library for two distinct purposes — keep them straight when changing analyzer code:

1. **`analyse sdk`** (the normal, correct flow): MDM exports an SDK folder, then hands it to this analyzer's file-backed validators (`SdkLoader`, `SourceKind.FILE`). MDM never touches analyzer domain objects directly here.
2. **`analyse emd`** (a diagnostic shortcut, `eu.europa.ted.mdc.service.analysis.emd.*` in mdm-lib): constructs this project's domain objects (`FieldsAndNodes`, `Field`, `FieldPrivacy`, `NoticeTypeContent`, `Codelist`, `Label`, etc.) directly from the live database, bypassing the SDK export, so Drools rules can run against the DB before export. This is the *only* place mdm-lib is meant to import this project's `domain.*` types, and it's a deliberate, precise dependency, not a leak — any change to a class/enum/setter in `domain.*` that mdm-lib's converters construct or call can break mdm-lib's build, so it needs care, not avoidance.

**Direct references to this project's `domain.*` enums from mdm-lib's converters are fine, and often the right call, for the enums that actually exist today.** Several of this project's setters are typed with their own enum and have no `String` alternative (`CodeListPropertyValue.setType(CodeListType)`, `NoticeSubTypeForIndex.setLegalBasis(NoticeLegalBasis)`, `NoticeTypeContent.setDisplayType(NoticeTypeContentDisplayType)`) — to call them at all, the caller must produce an instance of that enum. That's a *good* dependency: a literal mdm-lib's DB layer still emits but this project's enum no longer recognises then fails mdm-lib's **build**, immediately, with a clear location — instead of surfacing later as a runtime `IllegalArgumentException` deep inside a live `analyse emd` run against production data. Don't "fix" these by loosening the setter to accept a plain `String` just to remove the coupling; the coupling is the point.

### When should a `domain.*` property be an enum vs. a plain `String`?

The MDD database is the source of truth for enumerated values (e.g. codelist codes, privacy codes). It enforces validity via real DB constraints (a MySQL `ENUM` column), and JOOQ auto-generates a matching Java enum from that constraint for MDM's own code — that duplication is unavoidable and free (JOOQ regenerates it, nobody hand-maintains it).

A hand-maintained mirror of that same enum inside this project's `domain.*` package is a **third, independently-maintained copy** of the same source of truth, with no auto-sync — exactly the setup that caused the `FieldPrivacyCode` break (TEDEFO-5149 shrank the DB enum, this project's copy went stale, mdm-lib depended on the stale copy, and the mismatch didn't surface until CI). Avoid adding one unless it earns its keep:

- **Type it as an enum** only when a `.drl` rule actually pattern-matches specific constants of it (e.g. `noticeTypeRules.drl` matches `$displayType == NoticeTypeContentDisplayType.COMBOBOX` for TEDEFO-4664 — that's a real reason for `NoticeTypeContentDisplayType` to be an enum: the rule logic depends on which specific value it is).
- **Type it as a plain `String`** when this project only stores/passes the value through without ever branching on a specific constant in a rule — validity is someone else's job (the DB `ENUM`, or an SDK codelist file if a check is ever needed against SDK content itself). This is why `FieldPrivacy.code` is a `String`, not an enum (TEDEFO-5155): no rule in this project matches its value, so there was nothing for a project-local enum to buy beyond restating the DB's constraint a second time.
- If a future rule genuinely needs to branch on specific privacy-code (or similar) values, prefer checking against an SDK codelist loaded from the SDK content itself (the pattern used by "Code referenced in privacy property is in the expected codelist") over reintroducing a hardcoded/DB-mirrored enum — it stays self-contained and portable to any SDK folder, DB-backed or not.
- Before adding a new `domain.*` enum, grep every `.drl` file for the type name to confirm a rule will actually match against its constants. If none do, use `String`.

There is also a more fundamental reason this project should not mirror MDM's enums beyond that litmus test: this project's input is always **content already exported from the SDK** — the same file-backed path whether it comes from a real SDK folder or from `analyse emd`'s in-memory reconstruction of what an export would contain. MDM's own domain objects, by contrast, sit closer to raw UI/DB input, which can be dirtier (a user-submitted string before it is validated, a value mid-migration, etc.) — that is precisely the layer where DB `ENUM` constraints and JOOQ enums earn their keep. This project is not a second database and should not try to re-validate what MDM already guarantees before content ever reaches an SDK export; its job is checking *consistency within* already-exported SDK content (do these two files agree, does this reference resolve), not re-policing values MDM already policed upstream.

**What went wrong (TEDEFO-5153, TEDEFO-5145) was not "mdm-lib depends on our enums" — it was a genuine mismatch between what mdm-lib expected and what this project actually exposes at the time mdm-lib was built:**
- `NoticeTypeContent.setUnpublishGroupId/setUnpublishFieldId/setUnpublishCode` were removed from this project (the old per-field withholding scheme was dead), but `mdm-lib`'s `NoticeTypesConverter` still called them — a real cross-repo compile break, only caught in CI.
- `FieldPrivacy`'s privacy code went the other way: it used to be typed with a `domain.mdd.enums.FieldPrivacyCode` enum, and TEDEFO-5155 deliberately removed that enum, changing `getCode()`/`setCode()` to a plain `String` (mirroring the DB, which stores the code as free text validated by MDM's own JOOQ enum, not by this project). `mdm-lib`'s converter still referenced the now-deleted enum — the fix on mdm-lib's side is to pass the literal `String` straight through, since there is nothing left here to validate against.
- Because mdm-lib's `pom.xml` pins a *floating* SNAPSHOT version (`1.16.0-SNAPSHOT`) rather than a specific build, whichever jar happens to be cached locally vs. resolved by CI can disagree — a break can compile locally and fail in CI, or the reverse.

**Before removing, renaming, or retyping anything in `domain.*` (fields it exposes, setters, enum values):**
- Grep `eforms-metadata-manager/src/mdm-lib/src/main/java/eu/europa/ted/mdc/service/analysis/emd/` for usages of the type/method you're changing.
- If it's used there and the underlying feature is genuinely dead (as with `unpublishGroupId` et al.), remove the corresponding mdm-lib code in the same change/PR pair — don't leave mdm-lib broken for someone else to discover via a failed CI build.
- If you're removing an enum in favour of a plain `String` (as with `FieldPrivacyCode`), that's fine when the value's validity is already guaranteed upstream (e.g. by a DB-backed enum on the MDM side) — but update mdm-lib's converter in the same change to stop referencing the now-deleted type, rather than leaving a dangling `import`.
- Don't assume from memory whether a type still exists — check the actual current source (`git show origin/develop:<path>`, or grep the checked-out tree) before concluding it was "never merged" vs. "removed."
