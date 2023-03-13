@tedefo-1818
Feature: Notice Types - Document types schemaLocation validation
For every documentType check that the schemaLocation exists in the SDK.
  TEDEFO-1818: The schemaLocation of every documentType should exist in the SDK.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1818"

  Background:
    Given The following rules
      | Document types use existing schemaLocation |

  Scenario: All schemaLocation point to existing files
    Given A "tedefo-1818" folder with "valid" files
    When I load all document types
    And I execute validation 
    Then I should get 0 validation errors

  Scenario: Some schemaLocation do not point to existing files
    Given A "tedefo-1818" folder with "invalid" files
    When I load all document types
    And I execute validation 
    Then I should get 4 validation errors
