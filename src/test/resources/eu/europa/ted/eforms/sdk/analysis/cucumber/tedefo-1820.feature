@tedefo-1820
Feature: Notice Types - Fields validation
  TEDEFO-1820: Τhe field referenced by each content entry with type="field" should exist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1820"

  Background:
    Given The following rules
      | Notice sub types reference existing fields |

  Scenario: All referenced fields exist
    Given A "tedefo-1820" folder with "valid" files
    When I load all notice types
    And I load all fields
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some referenced fields do not exist
    Given A "tedefo-1820" folder with "invalid" files
    When I load all notice types
    And I load all fields
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 7 SDK validation errors

    Examples:
     | expected rule                              |
     | Notice sub types reference existing fields |
