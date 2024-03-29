@tedefo-2205
Feature: Fields and Nodes - Validate absolute xpath uniqueness
  TEDEFO-2205: All absolute XPaths are unique across all nodes and fields
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-2205"

  Background:
    Given The following rules
      | Absolute XPath of field is not equal to the absolute XPath of a node |
      | Absolute XPath of field is not equal to the absolute XPath of another field |
      | Absolute XPath of node is not equal to the absolute XPath of another node |

  Scenario: All absolute xpath are unique
    Given A "tedefo-2205" folder with "valid" files
    When I load all nodes
    And I load all fields
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some absolute xpath are not unique
    Given A "tedefo-2205" folder with "invalid" files
    When I load all nodes
    And I load all fields
    And I execute validation 
    Then Rule "<expected rule>" should have been fired 
    Then I should get 3 SDK validation errors

    Examples:
     | expected rule                         |
     | Absolute XPath of field is not equal to the absolute XPath of a node |
     | Absolute XPath of field is not equal to the absolute XPath of another field |
     | Absolute XPath of node is not equal to the absolute XPath of another node |
