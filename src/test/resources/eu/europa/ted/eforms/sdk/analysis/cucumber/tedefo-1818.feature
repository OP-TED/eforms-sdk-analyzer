@tedefo-1818
Feature: Notice Types Index - Document types schemaLocation validation
  TEDEFO-1818: The schemaLocation of every document type should exist in the SDK.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1818"

  Background:
    Given The following rules
      | Document types use existing schemaLocation |

  Scenario: All schemaLocation point to existing files
    Given A "tedefo-1818" folder with "valid" files
    When I load all document types
    And I execute validation 
    Then I should get 0 SDK validation errors

  Scenario Outline: Some schemaLocation do not point to existing files
    Given A "tedefo-1818" folder with "invalid" files
    When I load all document types
    And I execute validation 
    Then Rule "<expected rule>" should have been fired
    Then I should get 4 SDK validation errors

    Examples:
     | expected rule                              |
     | Document types use existing schemaLocation |
