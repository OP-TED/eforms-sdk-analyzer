@tedefo-1799
Feature: Notice examples - SVRL report validation
  TEDEFO-1799: All notice examples should have a corresponding validation report
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1799"

  Background:
    Given The following rules
      | Every notice example has a corresponding validation report  |
      | Every validation report has a corresponding notice example |

  Scenario: All notice examples and validation reports correspond
    Given A "tedefo-1799" folder with "valid" files
    When I load all notice examples
    And I load all SVRL reports
    And I load SDK metadata
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Notice examples and validation reports do not correspond
    Given A "tedefo-1799" folder with "invalid" files
    When I load all notice examples
    And I load all SVRL reports
    And I load SDK metadata
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
     | expected rule                      |
      | Every notice example has a corresponding validation report |
      | Every validation report has a corresponding notice example |
