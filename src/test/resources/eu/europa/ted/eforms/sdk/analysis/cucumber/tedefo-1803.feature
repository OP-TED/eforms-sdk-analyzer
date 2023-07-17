@tedefo-1803
Feature: Fields - node structure validation
  TEDEFO-1803: The node referenced by a parent ID exists
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1803"

  Background:
    Given The following rules
      | Every node parent ID corresponds to an existing node |
	  | Every field parent ID corresponds to an existing node |

  Scenario: Each node has at least one child node or field
    Given A "tedefo-1803" folder with "valid" files
    When I load all nodes
    And I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some nodes do not have any child
    Given A "tedefo-1803" folder with "invalid" files
    When I load all nodes
    And I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
      | expected rule                     |
      | Every node parent ID corresponds to an existing node |
	  | Every field parent ID corresponds to an existing node |
