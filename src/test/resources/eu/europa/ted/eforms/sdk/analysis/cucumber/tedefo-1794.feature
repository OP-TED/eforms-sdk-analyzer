@tedefo-1794
Feature: Codelists - Every code is unique in the codelist
  TEDEFO-1794: Every code is unique in the codelist
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1794"

  Background:
    Given The following rules
      | Every code is unique in the codelist |

  Scenario: Every code is unique in the codelist
    Given A "tedefo-1794" folder with "valid" files
    When I load all codelists
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Codelist contains duplicate codes
    Given A "tedefo-1794" folder with "invalid" files
    When I load all codelists
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 1 SDK validation errors

    Examples:
     | expected rule                                 |
     | Every code is unique in the codelist |
