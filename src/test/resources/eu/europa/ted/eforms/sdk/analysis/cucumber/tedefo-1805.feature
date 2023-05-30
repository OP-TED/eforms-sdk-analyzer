@tedefo-1805
Feature: Fields - XPaths consistency validation
  TEDEFO-1805: The node referenced by a parent ID exists
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1805"

  Background:
    Given The following rules
      | The absolute XPath of every node is consistent with its parent and its relative XPath |
      | The absolute XPath of every field is consistent with its parent and its relative XPath |

  Scenario: All absolute and relative XPaths are consistent
    Given A "tedefo-1805" folder with "valid" files
    When I load all nodes
    And I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some absolute and relative XPaths are not consistent
    Given A "tedefo-1805" folder with "invalid" files
    When I load all nodes
    And I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 5 SDK validation errors

    Examples:
      | expected rule                     |
      | The absolute XPath of every node is consistent with its parent and its relative XPath |
      | The absolute XPath of every field is consistent with its parent and its relative XPath |
