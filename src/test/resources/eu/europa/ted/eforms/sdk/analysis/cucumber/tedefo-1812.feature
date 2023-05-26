@tedefo-1812
Feature: Fields - Field validation
  TEDEFO-1812: Code referenced in privacy property is in the expected codelist
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1812"

  Background:
    Given The following rules
      | Code referenced in privacy property is in the expected codelist |

  Scenario: Code referenced in privacy property is in the expected codelist
    Given A "tedefo-1812" folder with "valid" files
    When I load all fields
	And I load all codelists
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Code referenced in privacy property is not in the expected codelist
    Given A "tedefo-1812" folder with "invalid" files
    When I load all fields
    And I load all codelists
	And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 1 SDK validation errors

    Examples:
      | expected rule                     |
      | Code referenced in privacy property is in the expected codelist |
