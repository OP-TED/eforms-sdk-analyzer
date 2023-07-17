@tedefo-2447
Feature: Fields - Field validation
  TEDEFO-2447: Fields have consistent information on XML attributes
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2447"

  Background:
    Given The following rules
      | Fields corresponding to XML attributes have the expected information |
      | Relationships between fields for element and attribute are consistent |

  Scenario: All fields have consistent information on XML attributes
    Given A "tedefo-2447" folder with "valid" files
    When I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some fields have incorrect information on XML attributes
    Given A "tedefo-2447" folder with "invalid" files
    When I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 4 SDK validation errors

    Examples:
      | expected rule                     |
      | Fields corresponding to XML attributes have the expected information |
      | Relationships between fields for element and attribute are consistent |
