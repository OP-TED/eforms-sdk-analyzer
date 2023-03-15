@tedefo-1821
Feature: Notice Types - Repeatable groups validation
  TEDEFO-1821: All groups marked as repeatable should should point to an existing node, which is also repeatable. 
	 Also, the first repeatable parent of that node should be associated with the first repeatable parent of the group 
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1821"

  Background:
    Given The following rules
      | Every repeatable group of notice sub types has a nodeId |
      | Every repeatable group of notice sub types references an existing repeatable node |
      | The first repeatable parent of a node is associated with the first repeatable parent of the referencing group |

  Scenario: All repeatable groups are valid
    Given A "tedefo-1821" folder with "valid" files
    When I load all notice types
    And I load all nodes
    And I execute validation 
    Then I should get 0 validation errors

  Scenario: Some referenced fields do not exist
    Given A "tedefo-1821" folder with "invalid" files
    When I load all notice types
    And I load all nodes
    And I execute validation 
    Then I should get 11 validation errors
