@tedefo-1797
Feature: Notice examples - SDK version validation
  TEDEFO-1797: All notice examples should have a CustomizationID that matches the SDK version
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1797"

  Background:
    Given The following rules
      | Every notice example has correct SDK version  |

  Scenario: All files have correct SDK version
    Given A "tedefo-1797" folder with "valid" files
    When I load all notice examples
    And I load SDK project information
    And I execute validation
    Then I should get 0 validation errors

  Scenario Outline: Some files do not have correct SDK version
    Given A "tedefo-1797" folder with "invalid" files
    When I load all notice examples
    And I load SDK project information
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 1 validation error

    Examples:
     | expected rule                      |
     | Every notice example has correct SDK version  |
