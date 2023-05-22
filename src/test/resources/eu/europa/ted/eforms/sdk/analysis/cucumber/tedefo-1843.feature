@tedefo-1843
Feature: Notice Types Index - Validate type values
  TEDEFO-1843: All type values are part of the 'notice-type' codelist.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1843"

  Background:
    Given The following rules
      | All type values are part of the notice-type codelist |

  Scenario: All formType values are valid
    Given A "tedefo-1843" folder with "valid" files
    When I load the notice types index
    And I load all codelists
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some formType values are not valid
    Given A "tedefo-1843" folder with "invalid" files
    When I load the notice types index
    And I load all codelists
    And I execute validation 
    Then Rule "<expected rule>" should have been fired 
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                                  |
     | All type values are part of the notice-type codelist |
