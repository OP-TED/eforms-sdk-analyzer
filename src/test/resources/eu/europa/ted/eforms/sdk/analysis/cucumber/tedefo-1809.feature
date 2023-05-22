@tedefo-1809
Feature: Fields and Nodes - Validate EFX expressions
  TEDEFO-1809: All EFX expressions in field constraints should be valid
  (they should be successfully translated to xpath using the EFX toolkit)
  Test files under "src/test/resources/eforms-sdk-tests/efx"

  Scenario: Some EFX expressions are invalid
    Given A "efx" folder with "" files
    When I execute EFX "expressions" validation
    Then I should get 3 EFX validation errors
