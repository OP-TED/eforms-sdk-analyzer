@tedefo-2459
Feature: Fields - Field validation
  TEDEFO-2459: Relative XPath does not contain ascending step
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2459"

  Background:
    Given The following rules
      | Relative XPath of field does not contain an ascending step |
      | Relative XPath of node does not contain an ascending step |

  Scenario: No relative XPath contain ascending steps
    Given A "tedefo-2459" folder with "valid" files
    When I load all fields
    When I load all nodes
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some XPath contain ascending steps
    Given A "tedefo-2459" folder with "invalid" files
    When I load all fields
    When I load all nodes
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 4 SDK validation errors

    Examples:
      | expected rule                     |
      | Relative XPath of field does not contain an ascending step |
      | Relative XPath of node does not contain an ascending step |
