@tedefo-4664
Feature: Notice Types - Check field display type
  TEDEFO-4664: The display type is consistent with the field type
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-4664"

  Background:
    Given The following rules
      | The displayType COMBOBOX is consistent with the type of the field |
      | The displayType RADIO is consistent with the type of the field |
      | The displayType TEXTAREA is consistent with the type of the field |
      | The displayType TEXTBOX is consistent with the type of the field |

  Scenario: Information for display types is correct
    Given A "tedefo-4664" folder with "valid" files
    When I load all notice types
    And I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario Outline: Information for display types is not correct
    Given A "tedefo-4664" folder with "invalid" files
    When I load all notice types
    And I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 4 SDK validation errors

    Examples:
      | expected rule                     |
      | The displayType COMBOBOX is consistent with the type of the field |
      | The displayType RADIO is consistent with the type of the field |
      | The displayType TEXTAREA is consistent with the type of the field |
      | The displayType TEXTBOX is consistent with the type of the field |
