@tedefo-1790
Feature: Codelists - Consistency of tailored codelists
  TEDEFO-1790: Codes in tailored codelists are present in parent codelist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1790"

  Background:
    Given The following rules
      | Codes in tailored codelists are present in parent codelist |

  Scenario: All codes in tailored codelists are present in parent codelist
    Given A "tedefo-1790" folder with "valid" files
    When I load all codelists
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some codes in tailored codelists are not present in parent codelist
    Given A "tedefo-1790" folder with "invalid" files
    When I load all codelists
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                 |
     | Codes in tailored codelists are present in parent codelist |
