@tedefo-1810
Feature: Fields - Label references validation
  TEDEFO-1810: Τhe labels referenced in the fields.json file should exist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1810"

  Background:
    Given The following rules
      | The labels referenced in field properties exist |

  Scenario: All referenced labels exist
    Given A "tedefo-1810" folder with "valid" files
    When I load all fields
    And I load all labels
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some referenced labels do not exist
    Given A "tedefo-1810" folder with "invalid" files
    When I load all fields
    And I load all labels
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                 |
     | The labels referenced in field properties exist |
