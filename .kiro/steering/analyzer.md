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
2. **`analyse emd`** (a diagnostic shortcut, `eu.europa.ted.mdc.service.analysis.emd.*` in mdm-lib): constructs this project's domain objects (`FieldsAndNodes`, `Field`, `FieldPrivacy`, `NoticeTypeContent`, `Codelist`, `Label`, etc.) directly from the live database, bypassing the SDK export, so Drools rules can run against the DB before export. This is the fragile path — any change to a class/enum/setter in `domain.*` that mdm-lib's converters construct or call can break mdm-lib's build.

**What went wrong (TEDEFO-5153, TEDEFO-5145):**
- `NoticeTypeContent.setUnpublishGroupId/setUnpublishFieldId/setUnpublishCode` were removed from this project (the old per-field withholding scheme was dead), but `mdm-lib`'s `NoticeTypesConverter` still called them — a real cross-repo compile break, only caught in CI.
- A `FieldPrivacyCode` enum was added to `domain.mdd.enums` on a feature branch and referenced from mdm-lib's `FieldsAndNodesConverter`, but never merged/published to a stable snapshot. mdm-lib's `pom.xml` pins a *floating* SNAPSHOT version (`1.16.0-SNAPSHOT`), so whether the build succeeds depended on which snapshot CI happened to resolve — not deterministic, and broke unpredictably. The type also duplicated validation mdm-lib's own DB-backed `FieldPrivacyCode` (JOOQ enum) already guaranteed — never add an analyzer-side enum purely so mdm-lib can re-validate a value that's already constrained upstream.

**Before removing or renaming anything in `domain.*` (fields it exposes, setters, enum values):**
- Grep `eforms-metadata-manager/src/mdm-lib/src/main/java/eu/europa/ted/mdc/service/analysis/emd/` for usages of the type/method you're changing.
- If it's used there and the underlying feature is genuinely dead (as with `unpublishGroupId` et al.), remove the corresponding mdm-lib code in the same change/PR pair — don't leave mdm-lib broken for someone else to discover via a failed CI build.
- Prefer plain types (`String`, not a bespoke enum) on domain classes that mdm-lib constructs, unless the analyzer's own rules need the stronger type. Every additional analyzer-side type is another thing mdm-lib has to stay in lockstep with across two independently-versioned repos.
