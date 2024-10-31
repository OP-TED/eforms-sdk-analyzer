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
import jakarta.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import eu.europa.ted.eforms.sdk.analysis.FactsLoader;
import eu.europa.ted.eforms.sdk.analysis.util.SdkMetadataParser;
import eu.europa.ted.eforms.sdk.analysis.validator.EfxValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SchematronValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.SdkValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.TextValidator;
import eu.europa.ted.eforms.sdk.analysis.validator.XmlSchemaValidator;
import eu.europa.ted.eforms.sdk.analysis.vo.SdkMetadata;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SdkValidationSteps {
  private static final Logger logger = LoggerFactory.getLogger(SdkValidationSteps.class);

  private Path testsFolder;

  private SdkValidator sdkValidator;
  private EfxValidator efxValidator;
  private TextValidator textValidator;
  private XmlSchemaValidator schemaValidator;
  private SchematronValidator schematronValidator;

  private List<String> testedRules = new ArrayList<>();

  private Exception thrownException = null;

  @Given("A {string} folder with {string} files")
  public void a_folder_with_files(String testsFolder, String filesValidity)
      throws URISyntaxException, IOException, JAXBException, SAXException,
      ParserConfigurationException {
    this.testsFolder = Path.of(getClass()
        .getResource(MessageFormat.format("/eforms-sdk-tests/{0}/{1}", testsFolder, filesValidity))
        .toURI());

    sdkValidator = new SdkValidator(this.testsFolder);
  }

  @Given("The following rules")
  public void the_following_rules(DataTable testedRules) {
    this.testedRules = testedRules.asList();
  }

  @When("I load all notice types")
  public void i_load_all_notice_types() throws IOException {
    sdkValidator.getSdkUnit().setNoticeTypes(new FactsLoader(testsFolder).loadNoticeTypes());
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
    sdkValidator.getSdkUnit().setNoticeTypesIndex(new FactsLoader(testsFolder).loadNoticeTypesIndex());
  }

  @When("I load all labels")
  public void i_load_all_labels()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkValidator.getSdkUnit().setLabels(new FactsLoader(testsFolder).loadLabels());
  }

  @When("I load the view templates index")
  public void i_load_the_view_templates_index()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkValidator.getSdkUnit().setViewTemplatesIndex(new FactsLoader(testsFolder).loadViewTemplatesIndex());
  }

  @When("I load the view templates")
  public void i_load_the_view_templates()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkValidator.getSdkUnit().setViewTemplates(new FactsLoader(testsFolder).loadViewTemplates());
  }

  @When("I load all document types")
  public void i_load_all_document_types() throws IOException {
    sdkValidator.getSdkUnit().setDocumentTypes(new FactsLoader(testsFolder).loadDocumentTypes());
  }

  @When("I load metadata from fields.json")
  public void I_load_fields_json() throws IOException {
    sdkValidator.getSdkUnit().setFieldsAndNodesMetadata(new FactsLoader(testsFolder).loadFieldsAndNodesMetadata());
  }

  @When("I load all fields")
  public void i_load_all_fields() throws IOException {
    sdkValidator.getSdkUnit().setFields(new FactsLoader(testsFolder).loadFields());
  }

  @When("I load all business entities")
  public void i_load_all_business_entities() throws IOException {
    sdkValidator.getSdkUnit().setBusinessEntities(new FactsLoader(testsFolder).loadBusinessEntities());
  }

  @When("I load all nodes")
  public void i_load_all_nodes() throws IOException {
    sdkValidator.getSdkUnit().setNodes(new FactsLoader(testsFolder).loadNodes());
  }

  @When("I load all codelists")
  public void i_load_all_codelists()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkValidator.getSdkUnit().setCodelists(new FactsLoader(testsFolder).loadCodelists());
  }

  @When("I load the codelists index")
  public void i_load_the_codelists_index()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkValidator.getSdkUnit().setCodelistsIndex(new FactsLoader(testsFolder).loadCodelistsIndex());
  }

  @When("I load all notice examples")
  public void I_load_all_notice_examples()
      throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
    sdkValidator.getSdkUnit().setXmlNotices(new FactsLoader(testsFolder).loadXmlNotices());
  }

  @When("I load all SVRL reports")
  public void I_load_all_SVRL_reports()
      throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
    sdkValidator.getSdkUnit().setSvrlReports(new FactsLoader(testsFolder).loadSvrlReports());
  }

  @When("I load all schematron files")
  public void I_load_all_Schematron_files() {
    sdkValidator.getSdkUnit().setSchematrons(new FactsLoader(testsFolder).loadSchematrons());
  }

  @When("I load SDK metadata")
  public void I_load_SDK_metadata() throws IOException {
    final SdkMetadata sdkMetadata = SdkMetadataParser.loadSdkMetadata(testsFolder);

    sdkValidator.getSdkUnit().setSdkMetadata(sdkMetadata);
  }

  @When("I execute validation")
  public void i_execute_validation()
      throws IOException, JAXBException, SAXException, ParserConfigurationException {
    sdkValidator.fireRules(testedRules.toArray(String[]::new));

    logger.info("Rules fired: {}",
        sdkValidator.getFiredRulesNames());

    if (sdkValidator.hasWarnings()) {
      logger.warn("Validation warnings:\n{}", StringUtils.join(sdkValidator.getWarnings(), '\n'));
    }

    if (sdkValidator.hasErrors()) { 
      logger.error("Validation errors:\n{}", StringUtils.join(sdkValidator.getErrors(), '\n'));
    }
  }

  @When("I execute EFX {string} validation")
  public void i_execute_efx_expressions_validation(final String validationType) throws IOException {

    efxValidator = new EfxValidator(testsFolder);
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

  @When("I execute schema validation")
  public void i_execute_schema_validation() throws IOException {
    schemaValidator = new XmlSchemaValidator(testsFolder);
    schemaValidator.validate();

    if (schemaValidator.hasWarnings()) {
      logger.warn("Validation warnings:\n{}",
          StringUtils.join(schemaValidator.getWarnings(), '\n'));
    }

    if (schemaValidator.hasErrors()) {
      logger.error("Validation errors:\n{}",
          StringUtils.join(schemaValidator.getErrors(), '\n'));
    }
  }

  @When("I execute text validation")
  public void i_execute_text_validation() throws Exception {
    textValidator = new TextValidator(testsFolder);
    textValidator.validate();

    if (textValidator.hasWarnings()) {
      logger.warn("Validation warnings:\n{}",
          StringUtils.join(textValidator.getWarnings(), '\n'));
    }

    if (textValidator.hasErrors()) {
      logger.error("Validation errors:\n{}",
          StringUtils.join(textValidator.getErrors(), '\n'));
    }
  }

  @When("I execute schematron validation")
  public void i_execute_schematron_validation() throws Exception {
    schematronValidator = new SchematronValidator(testsFolder);
    schematronValidator.validate();

    if (schematronValidator.hasWarnings()) {
      logger.warn("Validation warnings:\n{}",
          StringUtils.join(schematronValidator.getWarnings(), '\n'));
    }

    if (schematronValidator.hasErrors()) {
      logger.error("Validation errors:\n{}",
          StringUtils.join(schematronValidator.getErrors(), '\n'));
    }
  }

  @Then("No rules should have been fired")
  public void no_rules_should_have_been_fired() {
    assertEquals(0, sdkValidator.getFiredRulesNames().size());
  }

  @Then("Rule {string} should have been fired")
  public void rule_should_have_been_fired(String expectedRule) {
    assertTrue(sdkValidator.getFiredRulesNames().stream()
        .filter(firedRule -> firedRule.equals(expectedRule))
        .findAny()
        .isPresent());
  }

  @Then("^I should get (.*) SDK validation errors?$")
  public void i_should_get_sdk_validation_errors(int errorsCount) {
    assertEquals(errorsCount, sdkValidator.getErrors().size());
  }

  @Then("^I should get (.*) SDK validation warnings?$")
  public void i_should_get_sdk_validation_warnings(int warningsCount) {
    assertEquals(warningsCount, sdkValidator.getWarnings().size());
  }

  @Then("^I should get (.*) EFX validation errors?$")
  public void i_should_get_efx_validation_errors(int errorsCount) {
    assertEquals(errorsCount, efxValidator.getErrors().size());
  }

  @Then("^I should get (.*) schema validation errors?$")
  public void i_should_get_schema_validation_errors(int errorsCount) {
    assertEquals(errorsCount, schemaValidator.getErrors().size());
  }

  @Then("^I should get (.*) text validation errors?$")
  public void i_should_get_text_validation_errors(int errorsCount) {
    assertEquals(errorsCount, textValidator.getErrors().size());
  }

  @Then("^I should get (.*) schematron validation errors?$")
  public void i_should_get_schematron_validation_errors(int errorsCount) {
    assertEquals(errorsCount, schematronValidator.getErrors().size());
  }

  @Then("I should get not found exception for file {string}")
  public void i_should_get_not_found_exception_for_file(String fileName) {
    assertEquals(FileNotFoundException.class, thrownException.getClass());
    assertEquals(fileName, new File(thrownException.getMessage()).getName());
  }
}
