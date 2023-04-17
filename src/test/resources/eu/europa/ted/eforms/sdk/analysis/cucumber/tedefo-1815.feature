@tedefo-1815
Feature: Notice Types Index - Labels validation
  TEDEFO-1815: Τhe labels referenced in the notice-types.json file should exist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1815"

  Background:
    Given The following rules
      | Notice types index references existing labels |

  Scenario: All referenced labels exist
    Given A "tedefo-1815" folder with "valid" files
    When I load the notice types index
    And I load all labels
    And I execute validation 
    Then I should get 0 validation errors

  Scenario Outline: Some referenced labels do not exist
    Given A "tedefo-1815" folder with "invalid" files
    When I load the notice types index
    And I load all labels
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 validation errors

    Examples:
     | expected rule                                 |
     | Notice types index references existing labels |
