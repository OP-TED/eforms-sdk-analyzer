@tedefo-1842
Feature: Notice Types Index - Validate formType values
  TEDEFO-1842: All formType values are part of the 'form-type' codelist.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1842"

  Background:
    Given The following rules
      | All formType values are part of the form-type codelist |

  Scenario: All formType values are valid
    Given A "tedefo-1842" folder with "valid" files
    When I load the notice types index
    And I load all codelists
    And I execute validation 
    Then I should get 0 validation errors

  Scenario Outline: Some formType values are not valid
    Given A "tedefo-1842" folder with "invalid" files
    When I load the notice types index
    And I load all codelists
    And I execute validation 
    Then Rule "<expected rule>" should have been fired 
    Then I should get 3 validation errors

    Examples:
     | expected rule                                                  |
     | All formType values are part of the form-type codelist |
