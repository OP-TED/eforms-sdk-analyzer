@tedefo-1792
Feature: Codelists - Consistency of tailored codelists
  TEDEFO-1792: Codes in tailored codelists are present in parent codelist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1792"

  Background:
    Given The following rules
      | Codelist indicated in codelists index exists |
	  | Codelist files are indicated in codelists index |

  Scenario: All codes in tailored codelists are present in parent codelist
    Given A "tedefo-1792" folder with "valid" files
    When I load all codelists
	And I load the codelists index
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some codes in tailored codelists are not present in parent codelist
    Given A "tedefo-1792" folder with "invalid" files
    When I load all codelists
    And I load the codelists index
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                 |
     | Codelist indicated in codelists index exists |
	 | Codelist files are indicated in codelists index |
