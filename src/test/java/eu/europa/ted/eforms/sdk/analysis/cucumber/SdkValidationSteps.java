package eu.europa.ted.eforms.sdk.analysis.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.definition.rule.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import eu.europa.ted.eforms.sdk.analysis.EfxValidator;
import eu.europa.ted.eforms.sdk.analysis.FactsLoader;
import eu.europa.ted.eforms.sdk.analysis.SdkAnalyzer;
import eu.europa.ted.eforms.sdk.analysis.drools.SdkUnit;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;
import eu.europa.ted.eforms.sdk.util.SdkMetadataParser;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SdkValidationSteps {
  private static final Logger logger = LoggerFactory.getLogger(SdkValidationSteps.class);

  private Path testsFolder;

  private SdkUnit sdkUnit;
  private EfxValidator efxValidator;

  private List<String> testedRules = new ArrayList<>();

  private Exception thrownException = null;

  @Given("A {string} folder with {string} files")
  public void a_folder_with_files(String testsFolder, String filesValidity)
      throws URISyntaxException, IOException, JAXBException, SAXException,
      ParserConfigurationException {
    this.testsFolder = Path.of(getClass()
        .getResource(MessageFormat.format("/eforms-sdk-tests/{0}/{1}", testsFolder, filesValidity))
        .toURI());

    sdkUnit = new SdkUnit().setSdkRoot(this.testsFolder);
  }

  @Given("The following rules")
  public void the_following_rules(DataTable testedRules) {
    this.testedRules = testedRules.asList();
  }

  @When("I load all notice types")
  public void i_load_all_notice_types() throws IOException {
    sdkUnit.setNoticeTypes(new FactsLoader(testsFolder).loadNoticeTypes());
  }

  @When("I load all notice types storing the exception")
  public void i_load_all_notice_types_storing_the_exception() {
    try {
      i_load_all_notice_types();
    } catch (Exception e) {
      thrownException = e;
    }
  }

  @When("I load the notice types index")
  public void i_load_the_notice_types_index() throws IOException {
    sdkUnit.setNoticeTypesIndex(new FactsLoader(testsFolder).loadNoticeTypesIndex());
  }

  @When("I load all labels")
  public void i_load_all_labels()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkUnit.setLabels(new FactsLoader(testsFolder).loadLabels());
  }

  @When("I load the view templates index")
  public void i_load_the_view_templates_index()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkUnit.setViewTemplates(new FactsLoader(testsFolder).loadViewTemplates());
  }

  @When("I load all document types")
  public void i_load_all_document_types() throws IOException {
    sdkUnit.setDocumentTypes(new FactsLoader(testsFolder).loadDocumentTypes());
  }

  @When("I load metadata from fields.json")
  public void I_load_fields_json() throws IOException {
    sdkUnit.setFieldsAndNodesMetadata(new FactsLoader(testsFolder).loadFieldsAndNodesMetadata());
  }

  @When("I load all fields")
  public void i_load_all_fields() throws IOException {
    sdkUnit.setFields(new FactsLoader(testsFolder).loadFields());
  }

  @When("I load all nodes")
  public void i_load_all_nodes() throws IOException {
    sdkUnit.setNodes(new FactsLoader(testsFolder).loadNodes());
  }

  @When("I load all codelists")
  public void i_load_all_codelists()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkUnit.setCodelists(new FactsLoader(testsFolder).loadCodelists());
  }

  @When("I load all notice examples")
  public void I_load_all_notice_examples()
      throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
    sdkUnit.setXmlNotices(new FactsLoader(testsFolder).loadXmlNotices());
  }

  @When("I load all SVRL reports")
  public void I_load_all_SVRL_reports()
      throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
    sdkUnit.setSvrlReports(new FactsLoader(testsFolder).loadSvrlReports());
  }

  @When("I load SDK metadata")
  public void I_load_SDK_metadata() throws IOException {
    final SdkMetadata sdkMetadata = SdkMetadataParser.loadSdkMetadata(testsFolder);

    sdkUnit.setSdkMetadata(sdkMetadata);
  }

  @When("I execute validation")
  public void i_execute_validation()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    SdkAnalyzer.fireRules(sdkUnit, testedRules.toArray(String[]::new));

    logger.info("Rules fired: {}",
        sdkUnit.getFiredRules().stream().map(Rule::getName).collect(Collectors.toSet()));

    if (sdkUnit.hasWarnings()) {
      logger.warn("Validation warnings:\n{}", StringUtils.join(sdkUnit.getWarnings(), '\n'));
    }

    if (sdkUnit.hasErrors()) {
      logger.error("Validation errors:\n{}", StringUtils.join(sdkUnit.getErrors(), '\n'));
    }
  }

  @When("I execute EFX {string} validation")
  public void i_execute_efx_expressions_validation(final String validationType) throws IOException {
    final SdkMetadata sdkMetadata = SdkMetadataParser.loadSdkMetadata(testsFolder);

    efxValidator = new EfxValidator(testsFolder, sdkMetadata.getVersion());
    switch (validationType) {
      case "templates":
        efxValidator.validateTemplates();
        break;
      case "expressions":
        efxValidator.validateExpressions();
        break;
      default:
        throw new IllegalArgumentException(MessageFormat.format(
            "Unsupported validation type [{}]. Expected [templates] or [expressions]",
            validationType));
    }

    if (efxValidator.hasWarnings()) {
      logger.warn("Validation warnings:\n{}",
          StringUtils.join(efxValidator.getWarnings(), '\n'));
    }

    if (efxValidator.hasErrors()) {
      logger.error("Validation errors:\n{}",
          StringUtils.join(efxValidator.getErrors(), '\n'));
    }
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

  @Then("^I should get (.*) SDK validation errors?$")
  public void i_should_get_sdk_validation_errors(int errorsCount) {
    assertEquals(errorsCount, sdkUnit.getErrors().length);
  }

  @Then("^I should get (.*) EFX validation errors?$")
  public void i_should_get_efx_validation_errors(int errorsCount) {
    assertEquals(errorsCount, efxValidator.getErrors().length);
  }

  @Then("I should get not found exception for file {string}")
  public void i_should_get_not_found_exception_for_file(String fileName) {
    assertEquals(FileNotFoundException.class, thrownException.getClass());
    assertEquals(fileName, new File(thrownException.getMessage()).getName());
  }
}
