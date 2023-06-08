@tedefo-2336
Feature: Codelists - Parent code exists in codelist
  TEDEFO-2336: In hierarchical codelists, parent code exists in the codelist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-2336"

  Background:
    Given The following rules
      | Parent code exists in the codelist |

  Scenario: Parent code exists in the codelist
    Given A "tedefo-2336" folder with "valid" files
    When I load all codelists
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some parent codes do not exist in the codelist
    Given A "tedefo-2336" folder with "invalid" files
    When I load all codelists
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 1 SDK validation errors

    Examples:
     | expected rule                                 |
     | Parent code exists in the codelist |
