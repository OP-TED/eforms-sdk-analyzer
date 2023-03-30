@tedefo-1822
Feature: Notice Types - Ancestor groups validation for fields
  TEDEFO-1822: The first repeatable ancestor group of each field under a notice sub type should have a nodeId property
               that corresponds to a node that is an ancestor of the field.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1822"

  Background:
    Given The following rules
      | Ancestor groups and nodes of notice sub type fields are aligned |

  Scenario: All repeatable groups are valid
    Given A "tedefo-1822" folder with "valid" files
    When I load all notice types
    And I load all nodes
    And I load all fields
    And I execute validation 
    Then I should get 0 validation errors

  Scenario: Some referenced fields do not exist
    Given A "tedefo-1822" folder with "invalid" files
    When I load all notice types
    And I load all nodes
    And I load all fields
    And I execute validation 
    Then I should get 3 validation errors
