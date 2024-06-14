@tedefo-2767
Feature: Schematron - Check schematron files can be executed
  TEDEFO-2767: Check Schematron files against the schema
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2767"

  Scenario: Schematron files can be executed
    Given A "tedefo-2767" folder with "valid" files
    When I execute schematron validation
    Then I should get 0 schematron validation errors

  Scenario: Some schematron files cannot be executed
    Given A "tedefo-2767" folder with "invalid" files
    When I execute schematron validation
    Then I should get 1 schematron validation errors
