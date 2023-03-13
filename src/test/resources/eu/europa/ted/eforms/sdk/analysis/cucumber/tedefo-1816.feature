@tedefo-1816
Feature: Notice Types Index - View templates validation
  TEDEFO-1816: Τhe view templates referenced in the notice-types.json file should exist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1816"

  Background:
    Given The following rules
      | Notice types index references existing view templates |

  Scenario: All referenced view templates exist
    Given A "tedefo-1816" folder with "valid" files
    When I load the notice types index
    And I load the view templates index
    And I execute validation 
    Then I should get 0 validation errors

  Scenario: Some referenced view templates do not exist
    Given A "tedefo-1816" folder with "invalid" files
    When I load the notice types index
    And I load the view templates index
    And I execute validation 
    Then I should get 2 validation errors
