@tedefo-1802
Feature: Fields - node structure validation
  TEDEFO-1802: Every node has at least one child
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1802"

  Background:
    Given The following rules
      | Every node has at least one child |

  Scenario: Each node has at least one child node or field
    Given A "tedefo-1802" folder with "valid" files
    When I load all nodes
    And I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some nodes do not have any child
    Given A "tedefo-1802" folder with "invalid" files
    When I load all nodes
    And I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
      | expected rule                     |
      | Every node has at least one child |
