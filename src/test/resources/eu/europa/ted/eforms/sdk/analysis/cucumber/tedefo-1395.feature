@tedefo-1395
Feature: Codelists - Codes have a label for their name
  TEDEFO-1395: Codes have a label for their name
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1395"

  Background:
    Given The following rules
      | Every code has a label |

  Scenario: All labels for codes exist
    Given A "tedefo-1395" folder with "valid" files
    When I load all codelists
    And I load all labels
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some labels for codes do not exist
    Given A "tedefo-1395" folder with "invalid" files
    When I load all codelists
    And I load all labels
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                 |
     | Every code has a label |
