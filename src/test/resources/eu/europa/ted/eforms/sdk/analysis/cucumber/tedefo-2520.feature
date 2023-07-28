@tedefo-2520
Feature: Fields and Nodes - Validate EFX-2 expressions
  TEDEFO-2520: All EFX-2 expressions in field constraints should be valid
  (they should be successfully translated to xpath using the EFX toolkit)
  Test files under "src/test/resources/eforms-sdk-tests/efx-2"

  Scenario: Some EFX-2 expressions are invalid
    Given A "efx-2" folder with "" files
    When I execute EFX "expressions" validation
    Then I should get 1 EFX validation errors

  Scenario: Some EFX-2 templates are invalid
    Given A "efx-2" folder with "" files
    When I execute EFX "templates" validation
    Then I should get 1 EFX validation errors
