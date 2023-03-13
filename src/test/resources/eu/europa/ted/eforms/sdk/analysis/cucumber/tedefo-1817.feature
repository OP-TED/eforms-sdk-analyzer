@tedefo-1817
Feature: Notice Types Index - Document types validation
  TEDEFO-1817: The documentType of every noticeSubType exists in the documentTypes section of the notice types index
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1817"

  Background:
    Given The following rules
      | Notice sub types reference existing document types |

  Scenario: All referenced document types exist
    Given A "tedefo-1817" folder with "valid" files
    When I load the notice types index
    And I load all document types
    And I execute validation 
    Then I should get 0 validation errors

  Scenario: Some referenced document types do not exist
    Given A "tedefo-1817" folder with "invalid" files
    When I load the notice types index
    And I load all document types
    And I execute validation 
    Then I should get 2 validation errors
