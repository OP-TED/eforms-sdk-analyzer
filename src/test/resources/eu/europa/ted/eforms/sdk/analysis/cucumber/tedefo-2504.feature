@tedefo-2504
Feature: Notice Types - Validate privacy fields
  TEDEFO-2504: For every field, check that the notice subtype is not listed in the forbidden constraint that has no condition.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-2504"

  Background:
    Given The following rules
      | Privacy fields are present in the expected group |

  Scenario: All privacy fields are present in notice types
    Given A "tedefo-2504" folder with "valid" files
    When I load all notice types
    When I load all fields
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some privacy fields are missing in notice types
    Given A "tedefo-2504" folder with "invalid" files
    When I load all notice types
    When I load all fields
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 9 SDK validation errors

    Examples:
     | expected rule                                                  |
     | Privacy fields are present in the expected group |
