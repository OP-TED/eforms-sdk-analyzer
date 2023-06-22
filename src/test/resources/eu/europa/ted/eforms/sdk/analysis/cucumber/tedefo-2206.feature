@tedefo-2206
Feature: XML schema - Validate consistency with other files in SDK
  TEDEFO-2206: Repeatability of fields and nodes
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2206"

  Scenario: Schema allows repeatability of fields and nodes
    Given A "tedefo-2206" folder with "valid" files
    When I execute schema validation
    Then I should get 0 schema validation errors

  Scenario: Schema does not allow repeatability of fields and nodes
    Given A "tedefo-2206" folder with "invalid" files
    When I execute schema validation
    Then I should get 7 schema validation errors
