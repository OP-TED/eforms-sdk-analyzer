@tedefo-1892
Feature: Fields - Field validation
  TEDEFO-1892: Field is present in the notice type definitions for which it is mandatory
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1892"

  Background:
    Given The following rules
      | Field is present in the notice type definitions for which it is mandatory |

  Scenario: Fields are present in the notice type definitions for which they are mandatory
    Given A "tedefo-1892" folder with "valid" files
    When I load all fields
    And I load all notice types
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Fields are missing in the notice type definitions for which they are mandatory
    Given A "tedefo-1892" folder with "invalid" files
    When I load all fields
    And I load all notice types
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 3 SDK validation errors

    Examples:
      | expected rule                     |
      | Field is present in the notice type definitions for which it is mandatory |
