@tedefo-3877
Feature: View Templates Index - Labels validation
  TEDEFO-3877: Τhe labels referenced in the view-templates.json file should exist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-3877"

  Background:
    Given The following rules
      | View templates index references existing labels |

  Scenario: All referenced labels exist
    Given A "tedefo-3877" folder with "valid" files
    When I load the view templates index
    And I load all labels
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some referenced labels do not exist
    Given A "tedefo-3877" folder with "invalid" files
    When I load the view templates index
    And I load all labels
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                 |
     | View templates index references existing labels |
