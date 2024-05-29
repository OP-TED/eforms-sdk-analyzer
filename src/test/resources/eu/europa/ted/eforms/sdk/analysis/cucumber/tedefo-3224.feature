@tedefo-3224
Feature: Fields - business entity validation
  TEDEFO-3224: The business entity referenced by a field exists
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-3224"

  Background:
    Given The following rules
      | Business entities have a correct label identifier |
      | Relationships between business entities and nodes are consistent |
      | Relationships between business entities and identifier fields are consistent |
      | Relationships between business entities and caption fields are consistent |

  Scenario: Information for business entities is correct
    Given A "tedefo-3224" folder with "valid" files
    When I load all fields
    And I load all nodes
    And I load all business entities
    And I load all labels
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Information for business entities is not correct
    Given A "tedefo-3224" folder with "invalid" files
    When I load all fields
    And I load all nodes
    And I load all business entities
    And I load all labels
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 7 SDK validation errors

    Examples:
      | expected rule                     |
      | Business entities have a correct label identifier |
      | Relationships between business entities and nodes are consistent |
      | Relationships between business entities and identifier fields are consistent |
      | Relationships between business entities and caption fields are consistent |
