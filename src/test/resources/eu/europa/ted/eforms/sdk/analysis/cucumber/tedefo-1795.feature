@tedefo-1795
Feature: Codelists - Labels in codelists index
  TEDEFO-1795: Labels indicated in codelists index exists
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1795"

  Background:
    Given The following rules
      | Label indicated in codelists index exists |

  Scenario: All labels in codelist index exist
    Given A "tedefo-1795" folder with "valid" files
    When I load the codelists index
    And I load all labels
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some labels in codelist index do not exist
    Given A "tedefo-1795" folder with "invalid" files
    When I load the codelists index
    And I load all labels
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                 |
     | Label indicated in codelists index exists |
