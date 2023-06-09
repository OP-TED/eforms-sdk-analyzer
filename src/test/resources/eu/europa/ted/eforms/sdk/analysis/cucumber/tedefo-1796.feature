@tedefo-1796
Feature: Codelists - Rows correspond to column definition
  TEDEFO-1796: Every column reference exists in the column definition
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1796"

  Background:
    Given The following rules
      | Every column reference exists in the column definition |

  Scenario: Every column reference exists in the column definition
    Given A "tedefo-1796" folder with "valid" files
    When I load all codelists
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some column references do not exist in the column definition
    Given A "tedefo-1796" folder with "invalid" files
    When I load all codelists
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 21 SDK validation errors

    Examples:
     | expected rule                                 |
     | Every column reference exists in the column definition |
