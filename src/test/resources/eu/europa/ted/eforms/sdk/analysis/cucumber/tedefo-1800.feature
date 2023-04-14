@tedefo-1800
Feature: Notice examples - SVRL report validation
  TEDEFO-1800: Valid notice examples should have no errors in the validation report
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1800"

  Background:
    Given The following rules
      | Every valid notice example has no errors in the corresponding validation report |

  Scenario: All valid notice examples do not have any error
    Given A "tedefo-1800" folder with "valid" files
    When I load all notice examples
    And I load all SVRL reports
    And I load SDK metadata
    And I execute validation
    Then I should get 0 validation errors

  Scenario Outline: Valid notice examples have errors
    Given A "tedefo-1800" folder with "invalid" files
    When I load all notice examples
    And I load all SVRL reports
    And I load SDK metadata
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 1 validation error

    Examples:
     | expected rule                      |
      | Every valid notice example has no errors in the corresponding validation report |
