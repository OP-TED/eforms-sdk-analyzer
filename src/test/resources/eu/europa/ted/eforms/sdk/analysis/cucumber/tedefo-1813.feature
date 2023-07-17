@tedefo-1813
Feature: Notice Types - SDK version validation
  TEDEFO-1813: All notice type files should declare the SDK version
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1813"

  Background:
    Given The following rules
      | Every notice type has SDK version  |
      | Notice types index has SDK version |

  Scenario: All files have SDK version
    Given A "tedefo-1813" folder with "valid" files
    When I load all notice types
    And I load the notice types index
    And I execute validation 
    Then No rules should have been fired
    And I should get 0 SDK validation errors

  Scenario Outline: Some files do not have SDK version
    Given A "tedefo-1813" folder with "invalid" files
    When I load all notice types
    And I load the notice types index
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    And I should get 3 SDK validation errors

    Examples:
     | expected rule                      |
     | Every notice type has SDK version  |
     | Notice types index has SDK version |
