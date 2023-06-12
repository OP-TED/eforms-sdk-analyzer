@tedefo-1871
Feature: Translations - Label texts do not contain invalid characters
  TEDEFO-1871: Label texts do not contain invalid characters
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1871"

  Background:
    Given The following rules
      | Label texts do not contain invalid characters |

  Scenario: All labels for codes exist
    Given A "tedefo-1871" folder with "valid" files
    When I load all labels
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some labels for codes do not exist
    Given A "tedefo-1871" folder with "invalid" files
    When I load all labels
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                 |
     | Label texts do not contain invalid characters |
