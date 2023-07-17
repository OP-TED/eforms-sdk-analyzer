@tedefo-1811
Feature: Fields - Field validation
  TEDEFO-1811: Fields referenced in privacy property exist
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1811"

  Background:
    Given The following rules
      | Fields referenced in privacy property exist |

  Scenario: Fields referenced in privacy property exist
    Given A "tedefo-1811" folder with "valid" files
    When I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Fields referenced in privacy property do not exist
    Given A "tedefo-1811" folder with "invalid" files
    When I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
      | expected rule                     |
      | Fields referenced in privacy property exist |
