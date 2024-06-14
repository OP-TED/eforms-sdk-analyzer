@tedefo-3301
Feature: Translations - Label texts do not contain label identifiers
  TEDEFO-3301: Label texts do not contain label identifiers
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-3301"

  Scenario: All labels have only valid characters
    Given A "tedefo-3301" folder with "valid" files
    When I execute text validation 
    Then I should get 0 text validation errors

  Scenario: Some labels have text containing label identifiers
    Given A "tedefo-3301" folder with "invalid" files
    When I execute text validation 
    Then I should get 2 text validation errors
