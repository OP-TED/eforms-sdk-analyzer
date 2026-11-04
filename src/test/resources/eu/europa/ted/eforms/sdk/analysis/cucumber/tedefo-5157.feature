@tedefo-5157
Feature: Fields - business entity node relationship (many-to-one)
  TEDEFO-5157: A node may be shared by several business entities. The relationship
  only requires the node referenced by "repeatsWithNodeId" to exist, regardless of
  the node's own "businessEntityId".
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-5157"
  Background:
    Given The following rules
      | Relationships between business entities and nodes are consistent |
  Scenario: Several business entities share the same node
    Given A "tedefo-5157" folder with "valid" files
    When I load all nodes
    And I load all business entities
    And I execute validation
    Then I should get 0 SDK validation errors
  Scenario Outline: A business entity references a node that does not exist
    Given A "tedefo-5157" folder with "invalid" files
    When I load all nodes
    And I load all business entities
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 1 SDK validation error
    Examples:
      | expected rule                                                    |
      | Relationships between business entities and nodes are consistent |
