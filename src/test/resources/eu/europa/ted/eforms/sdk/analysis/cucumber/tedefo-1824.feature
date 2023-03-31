@tedefo-1824
Feature: Notice Types - Validate field constraints
  TEDEFO-1824: For every field, check that the notice subtype is not listed in the forbidden constraint that has no condition.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1824"

  Background:
    Given The following rules
      | Notice sub type is not unconditionally forbidden for any field |

  Scenario: No notice sub types are listed on their fields' unconditionally forbidden constraints
    Given A "tedefo-1824" folder with "valid" files
    When I load all notice types
    When I load all fields
    And I execute validation 
    Then I should get 0 validation errors

  Scenario: Some notice sub types are listed on their fields\' unconditionally forbidden constraints
    Given A "tedefo-1824" folder with "invalid" files
    When I load all notice types
    When I load all fields
    And I execute validation 
    Then I should get 4 validation errors
