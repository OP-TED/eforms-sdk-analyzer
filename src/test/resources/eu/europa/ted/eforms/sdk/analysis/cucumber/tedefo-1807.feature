@tedefo-1807
Feature: Fields - Field validation
  TEDEFO-1807: Codelist referenced in the codelist property exists
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1807"

  Background:
    Given The following rules
      | The codelist referenced in the codelist property exists |

  Scenario: The codelist referenced in the codelist property exists
    Given A "tedefo-1807" folder with "valid" files
    When I load all fields
	And I load all codelists
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: The codelist referenced in the codelist property does not exists
    Given A "tedefo-1807" folder with "invalid" files
    When I load all fields
    And I load all codelists
	And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 3 SDK validation errors

    Examples:
      | expected rule                     |
      | The codelist referenced in the codelist property exists |
