@tedefo-1793
Feature: Codelists - Presence of code column definition
  TEDEFO-1793: Every codelist has a code column
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1793"

  Background:
    Given The following rules
      | Every codelist has a column for the code |

  Scenario: The definition of the code column is present
    Given A "tedefo-1793" folder with "valid" files
    When I load all codelists
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: The definition of the code column is not present
    Given A "tedefo-1793" folder with "invalid" files
    When I load all codelists
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 3 SDK validation errors

    Examples:
     | expected rule                                 |
     | Every codelist has a column for the code |
