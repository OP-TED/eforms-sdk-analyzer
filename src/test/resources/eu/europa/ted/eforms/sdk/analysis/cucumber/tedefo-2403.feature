@tedefo-2403
Feature: Fields - Field validation
  TEDEFO-2403: Fields and nodes have XSD sequence order consistent with relative XPath
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2403"

  Background:
    Given The following rules
      | Every field has XSD sequence order consistent with relative xpath |
	  | Every non-root node has XSD sequence order consistent with relative xpath |

  Scenario: All fields and nodes have XSD sequence order consistent with relative xpath
    Given A "tedefo-2403" folder with "valid" files
    When I load all fields
    And I load all nodes
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some fields and nodes do not have XSD sequence order consistent with relative xpath
    Given A "tedefo-2403" folder with "invalid" files
    When I load all fields
    And I load all nodes
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 5 SDK validation errors

    Examples:
      | expected rule                     |
      | Every field has XSD sequence order consistent with relative xpath |
	  | Every non-root node has XSD sequence order consistent with relative xpath |
