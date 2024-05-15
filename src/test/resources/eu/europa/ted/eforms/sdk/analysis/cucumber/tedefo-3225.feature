@tedefo-3225
Feature: Notice Types - Check business entity
  TEDEFO-3225: The business entity is consistent with the node
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-3225"

  Background:
    Given The following rules
      | Business entity of the group is consistent with the node |

  Scenario: Information for business entities is correct
    Given A "tedefo-3225" folder with "valid" files
    When I load all notice types
    And I load all business entities
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Information for business entities is not correct
    Given A "tedefo-3225" folder with "invalid" files
    When I load all notice types
    And I load all business entities
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
      | expected rule                     |
      | Business entity of the group is consistent with the node |
