@tedefo-1814
Feature: Notice Types Index - Notice type files validation
  TEDEFO-1814: A JSON file should exist for each noticeSubType in notice-types.json
  with a one-to-one correspondence.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1814"

  Scenario: A file exists for each notice type on the index file
    Given A "tedefo-1814" folder with "valid" files
    When I load all notice types
    Then I should get 0 SDK validation errors

  Scenario: Files do not exist for some notice types on the index file
    # The loader no longer aborts when an indexed notice subtype has no definition file:
    # it loads the definitions that are present and lets the rule report the missing one,
    # instead of throwing before any rule can run.
    Given The following rules
      | All expected notice subtypes are present |
    And A "tedefo-1814" folder with "invalid" files
    When I load the notice types index
    And I load all notice types
    And I execute validation
    Then Rule "All expected notice subtypes are present" should have been fired
