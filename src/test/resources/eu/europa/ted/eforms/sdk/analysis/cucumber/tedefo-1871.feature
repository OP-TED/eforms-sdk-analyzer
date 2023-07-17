@tedefo-1871
Feature: Translations - Label texts do not contain invalid characters
  TEDEFO-1871: Label texts do not contain invalid characters
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1871"

  Scenario: All labels have only valid characters
    Given A "tedefo-1871" folder with "valid" files
    When I execute text validation 
    Then I should get 0 text validation errors

  Scenario: Some labels have invalid characters
    Given A "tedefo-1871" folder with "invalid" files
    When I execute text validation 
    Then I should get 4 text validation errors
