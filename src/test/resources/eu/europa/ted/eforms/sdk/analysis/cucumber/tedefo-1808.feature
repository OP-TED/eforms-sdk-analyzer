@tedefo-1808
Feature: Fields - Notice type references validation
  TEDEFO-1808: The notice type referenced by constraints exists
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1808"

  Background:
    Given The following rules
      | The notice subtype referenced in field constraints exists |

  Scenario: All notice type referenced by constraints exists
    Given A "tedefo-1808" folder with "valid" files
    When I load all fields
	And I load all notice types
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some notice types referenced by constraints do not exist
    Given A "tedefo-1808" folder with "invalid" files
    When I load all fields
	And I load all notice types
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 8 SDK validation errors

    Examples:
      | expected rule                     |
      | The notice subtype referenced in field constraints exists |
