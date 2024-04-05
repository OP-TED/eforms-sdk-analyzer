@tedefo-2684
Feature: Schematron files - Assert id validation
  TEDEFO-2684: All asserts should have an id, with no duplicates
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2684"

  Background:
    Given The following rules
      | Every assert has an id |
      | Every assert id is unique in the schematron file |

  Scenario: All asserts have a unique id
    Given A "tedefo-2684" folder with "valid" files
    When I load all schematron files
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some asserts do not have an id or have a duplicate
    Given A "tedefo-2684" folder with "invalid" files
    When I load all schematron files
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
     | expected rule                      |
      | Every assert has an id |
      | Every assert id is unique in the schematron file |
