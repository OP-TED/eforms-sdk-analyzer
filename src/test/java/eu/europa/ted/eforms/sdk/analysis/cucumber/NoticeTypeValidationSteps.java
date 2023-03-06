package eu.europa.ted.eforms.sdk.analysis.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.kie.api.definition.rule.Rule;
import eu.europa.ted.eforms.sdk.analysis.FactsLoader;
import eu.europa.ted.eforms.sdk.analysis.drools.RulesRunner;
import eu.europa.ted.eforms.sdk.analysis.drools.SdkUnit;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class NoticeTypeValidationSteps {
  private Path testsFolder;

  private SdkUnit sdkUnit;
  private List<String> testedRules = new ArrayList<>();

  @Given("A {string} folder with {string} files")
  public void a_folder_with_files(String testsFolder, String filesValidity)
      throws URISyntaxException {
    this.testsFolder = Path.of(getClass()
        .getResource(MessageFormat.format("/eforms-sdk-tests/{0}/{1}", testsFolder, filesValidity))
        .toURI());

    sdkUnit = new SdkUnit();
  }

  @Given("The following rules")
  public void the_following_rules(DataTable testedRules) {
    this.testedRules = testedRules.asList();
  }

  @When("I load all notice types")
  public void i_load_all_notice_types() throws IOException {
    FactsLoader factsLoader = new FactsLoader(testsFolder);
    sdkUnit.setNoticeTypes(factsLoader.loadNoticeTypes());
  }

  @When("I load the notice types index")
  public void i_load_the_notice_types_index() throws IOException {
    FactsLoader factsLoader = new FactsLoader(testsFolder);
    sdkUnit.setNoticeTypesIndex(factsLoader.loadNoticeTypesIndex());
  }

  @When("I execute validation")
  public void i_execute_validation() {
    sdkUnit = RulesRunner.execute(sdkUnit, testedRules.toArray(String[]::new));
  }

  @Then("No rules should have been fired")
  public void no_rules_should_have_been_fired() {
    assertEquals(0, sdkUnit.getFiredRules().size());
  }

  @Then("Rule {string} should have been fired")
  public void rule_should_have_been_fired(String expectedRule) {
    assertTrue(sdkUnit.getFiredRules().stream()
        .filter((Rule firedRule) -> firedRule.getName().equals(expectedRule))
        .findAny()
        .isPresent());
  }

  @Then("^I should get (.*) validation errors$")
  public void i_should_get_validation_errors(int errorsCount) {
    assertEquals(errorsCount, sdkUnit.getErrors().length);
  }
}
