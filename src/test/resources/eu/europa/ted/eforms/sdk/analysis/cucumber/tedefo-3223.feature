@tedefo-3223
Feature: Fields - business entity validation
  TEDEFO-3223: The business entity referenced by a field exists
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-3223"

  Background:
    Given The following rules
      | All fields are part of an existing business entity |

  Scenario: All fields are part of an existing business entity
    Given A "tedefo-3223" folder with "valid" files
    When I load all fields
    And I load all business entities
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some fields are not part of an existing business entity
    Given A "tedefo-3223" folder with "invalid" files
    When I load all fields
    And I load all business entities
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 3 SDK validation errors

    Examples:
      | expected rule                     |
      | All fields are part of an existing business entity |
