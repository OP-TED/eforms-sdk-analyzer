@tedefo-2673
Feature: Fields - Field validation
  TEDEFO-2673: Value in schemeName is consistent with preset value in corresponding attribute field
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-2673"

  Background:
    Given The following rules
      | Fields with the schemeName property have a corresponding attribute field with the expected preset value |

  Scenario: All fields have consistent information on XML attributes
    Given A "tedefo-2673" folder with "valid" files
    When I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Some fields have incorrect information on XML attributes
    Given A "tedefo-2673" folder with "invalid" files
    When I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation errors

    Examples:
      | expected rule                     |
      | Fields with the schemeName property have a corresponding attribute field with the expected preset value |
