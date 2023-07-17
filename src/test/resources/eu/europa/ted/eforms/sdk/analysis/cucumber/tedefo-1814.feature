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
    Given A "tedefo-1814" folder with "invalid" files
    When I load all notice types storing the exception
    Then I should get not found exception for file "3.json"
