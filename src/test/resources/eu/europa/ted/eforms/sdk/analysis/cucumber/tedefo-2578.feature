@tedefo-2578
Feature: Notice Types Index - Check expected notice types definition
  TEDEFO-2578: All expected NTDs are present.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-2578"

  Background:
    Given The following rules
      | All expected notice subtypes are present |

  Scenario: All expected notice subtypes are present
    Given A "tedefo-2578" folder with "valid" files
    When I load the notice types index
    And I load all notice types
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some notice subtypes are missing
    Given A "tedefo-2578" folder with "invalid" files
    When I load the notice types index
    And I load all notice types
    And I execute validation 
    Then Rule "<expected rule>" should have been fired 
    Then I should get 2 SDK validation errors

    Examples:
     | expected rule                                                  |
     | All expected notice subtypes are present |
