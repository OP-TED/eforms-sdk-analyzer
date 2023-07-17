@tedefo-1823
Feature: Notice Types - Check for duplicate node ids
  TEDEFO-1823: For every notice sub type, a nodeId can only appear once (no 2 groups with the same nodeId).
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1823"

  Background:
    Given The following rules
      | No two notice sub type groups reference the same nodeId |

  Scenario: Node IDs are not duplicated
    Given A "tedefo-1823" folder with "valid" files
    When I load all notice types
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some node IDs are duplicated
    Given A "tedefo-1823" folder with "invalid" files
    When I load all notice types
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 4 SDK validation errors

    Examples:
     | expected rule                                           |
     | No two notice sub type groups reference the same nodeId |
