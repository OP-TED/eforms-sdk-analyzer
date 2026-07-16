package eu.europa.ted.eforms.sdk.analysis.validator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import eu.europa.ted.eforms.sdk.analysis.testutil.EmptySdkContentSource;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;

/**
 * Verifies that {@link SdkValidator} can be driven by an arbitrary
 * {@link eu.europa.ted.eforms.sdk.analysis.SdkContentSource}, without any
 * file system access.
 */
class SdkValidatorContentSourceTest {

  @Test
  void newConstructorRunsValidatePipelineWithoutFileSystem() throws Exception {
    final SdkValidator validator =
        new SdkValidator(new EmptySdkContentSource(), new SdkMetadata("1.15.0"));

    assertSame(validator, validator.validate());
    assertNotNull(validator.getResults());
    assertNotNull(validator.getFiredRulesNames());
  }
}
