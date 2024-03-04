@tedefo-2851
Feature: Translation - Check label identifiers
  TEDEFO-2851: Check for duplicate label identifiers.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-2851"

  Background:
    Given The following rules
      | There are no labels with similar identifiers |

  Scenario: There are no labels with similar identifiers
    Given A "tedefo-2851" folder with "valid" files
    When I load all labels
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some label identifiers are similar
    Given A "tedefo-2851" folder with "invalid" files
    When I load all labels
    And I execute validation 
    Then Rule "<expected rule>" should have been fired 
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                                  |
     | There are no labels with similar identifiers |
