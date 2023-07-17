@tedefo-1819
Feature: Notice Types - Labels validation
  TEDEFO-1819: Τhe labels referenced in each <noticeSubtype>.json file should exist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1819"

  Background:
    Given The following rules
      | Notice sub types reference existing labels |

  Scenario: All referenced labels exist
    Given A "tedefo-1819" folder with "valid" files
    When I load all notice types
    And I load all labels
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some referenced labels do not exist
    Given A "tedefo-1819" folder with "invalid" files
    When I load all notice types
    And I load all labels
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                              |
     | Notice sub types reference existing labels |
