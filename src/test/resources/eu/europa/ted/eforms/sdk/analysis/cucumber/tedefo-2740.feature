@tedefo-2740
Feature: Fields - Field validation
  TEDEFO-2740: Values in noticeTypes property are not repeated
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2740"

  Background:
    Given The following rules
      | Field constraints do not have duplicate notice types identifiers |

  Scenario: All fields have no duplicate notice types
    Given A "tedefo-2740" folder with "valid" files
    When I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some fields have duplicate notice types
    Given A "tedefo-2740" folder with "invalid" files
    When I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
      | expected rule                     |
      | Field constraints do not have duplicate notice types identifiers |
