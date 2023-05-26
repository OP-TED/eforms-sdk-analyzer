@tedefo-2280
Feature: Fields - Field validation
  TEDEFO-2280: Fields and nodes have an XSD sequence order
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2280"

  Background:
    Given The following rules
      | Every field has an XSD sequence order |
	  | Every non-root node has an XSD sequence order |

  Scenario: All fields and nodes have an XSD sequence order
    Given A "tedefo-2280" folder with "valid" files
    When I load all fields
	And I load all nodes
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some fields and nodes do not have an XSD sequence order
    Given A "tedefo-2280" folder with "invalid" files
    When I load all fields
    And I load all nodes
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
      | expected rule                     |
      | Every field has an XSD sequence order |
	  | Every non-root node has an XSD sequence order |
