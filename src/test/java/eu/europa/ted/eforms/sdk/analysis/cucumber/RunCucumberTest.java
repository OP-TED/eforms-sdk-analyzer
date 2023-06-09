package eu.europa.ted.eforms.sdk.analysis.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import io.cucumber.core.options.Constants;


@Suite
@SelectClasspathResource("eu/europa/ted/eforms/sdk/analysis")
@ConfigurationParameter(key = Constants.ANSI_COLORS_DISABLED_PROPERTY_NAME, value = "true")
public class RunCucumberTest {
}
