@tedefo-1801
Feature: Fields - SDK version validation
  TEDEFO-1801: The SDK version in fields json matches the version of the SDK
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1801"

  Background:
    Given The following rules
      | Fields.json has correct SDK version |

  Scenario: The SDK version in fields json matches the version of the SDK
    Given A "tedefo-1801" folder with "valid" files
    When I load metadata from fields.json
    And I load SDK metadata
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: The SDK version in fields json does not match the version of the SDK
    Given A "tedefo-1801" folder with "invalid" files
    When I load metadata from fields.json
    And I load SDK metadata
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 1 SDK validation errors

    Examples:
     | expected rule                      |
      | Fields.json has correct SDK version |
