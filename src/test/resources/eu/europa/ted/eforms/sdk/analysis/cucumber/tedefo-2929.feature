@tedefo-2929
Feature: Schematron - Check schematron files are well-formed
  TEDEFO-2929: Check Schematron files against the schema
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2929"

  Scenario: Schematron files are well-formed
    Given A "tedefo-2929" folder with "valid" files
    When I execute schematron validation
    Then I should get 0 schematron validation errors

  Scenario: Some schematron files are not well-formed
    Given A "tedefo-2929" folder with "invalid" files
    When I execute schematron validation
    Then I should get 4 schematron validation errors
