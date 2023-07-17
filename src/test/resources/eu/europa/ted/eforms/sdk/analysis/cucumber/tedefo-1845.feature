@tedefo-1845
Feature: Notice Types - Ancestor nodes validation for fields
  TEDEFO-1845: All repeatable ancestors of a field are in the notice type definition
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1845"

  Background:
    Given The following rules
      | All repeatable ancestors of a field are in the notice type definition |

  Scenario: All repeatable groups are valid
    Given A "tedefo-1845" folder with "valid" files
    When I load all notice types
    And I load all nodes
    And I load all fields
    And I execute validation 
    Then I should get 0 SDK validation warnings

  Scenario Outline: Some referenced fields do not exist
    Given A "tedefo-1845" folder with "invalid" files
    When I load all notice types
    And I load all nodes
    And I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    Then I should get 6 SDK validation warnings

    Examples:
     | expected rule                                                   |
     | All repeatable ancestors of a field are in the notice type definition |
