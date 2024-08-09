@tedefo-2768
Feature: Schematron files - Consistency validation
  TEDEFO-2768: Check schematron files are complete and consistent
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2768"

  Background:
    Given The following rules
      | All expected phases are present |
      | Every pattern is part of at least one phase |
      | Every assert diagnostics is defined in the schematron file |

  Scenario: All schematron files are consistent
    Given A "tedefo-2768" folder with "valid" files
    When I load all schematron files
    And I load all notice types
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some schematron files are inconsistent
    Given A "tedefo-2768" folder with "invalid" files
    When I load all schematron files
    And I load all notice types
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 5 SDK validation errors

    Examples:
      | expected rule                      |
      | All expected phases are present |
      | Every pattern is part of at least one phase |
      | Every assert diagnostics is defined in the schematron file |
