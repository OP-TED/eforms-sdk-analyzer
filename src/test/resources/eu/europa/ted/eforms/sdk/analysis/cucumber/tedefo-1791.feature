@tedefo-1791
Feature: Codelists - Filename convention
  TEDEFO-1791: Codelist filenames are as expected
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1791"

  Background:
    Given The following rules
      | Codelist filenames are as expected |

  Scenario: All codes in tailored codelists are present in parent codelist
    Given A "tedefo-1791" folder with "valid" files
    When I load all codelists
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some codes in tailored codelists are not present in parent codelist
    Given A "tedefo-1791" folder with "invalid" files
    When I load all codelists
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 3 SDK validation errors

    Examples:
     | expected rule                                 |
     | Codelist filenames are as expected |
