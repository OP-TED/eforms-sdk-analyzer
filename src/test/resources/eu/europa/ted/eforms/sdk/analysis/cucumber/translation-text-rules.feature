@translation-text-rules
Feature: Translation - Label text checks run as drools rules
  The translation-text checks (invalid characters and label-identifier leaks) run as drools rules
  inside SdkValidator. Reuses the fixtures under "src/test/resources/eforms-sdk-tests/tedefo-3301".

  Background:
    Given The following rules
      | Label text does not contain label identifiers |
      | Label text does not contain invalid characters |

  Scenario: Clean label texts produce no errors
    Given A "tedefo-3301" folder with "valid" files
    When I load all labels
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario: Label texts containing label identifiers are flagged
    Given A "tedefo-3301" folder with "invalid" files
    When I load all labels
    And I execute validation
    Then Rule "Label text does not contain label identifiers" should have been fired
    And I should get 2 SDK validation errors
