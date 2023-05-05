@tedefo-1804
Feature: Fields - node structure validation
  TEDEFO-1804: The node referenced by a parent ID exists
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1804"

  Background:
    Given The following rules
      | There are no loops in the node structure |

  Scenario: No node is part of their ancestors
    Given A "tedefo-1804" folder with "valid" files
    When I load all nodes
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some nodes are part of their ancestors
    Given A "tedefo-1804" folder with "invalid" files
    When I load all nodes
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 3 SDK validation errors

    Examples:
      | expected rule                     |
      | There are no loops in the node structure |
