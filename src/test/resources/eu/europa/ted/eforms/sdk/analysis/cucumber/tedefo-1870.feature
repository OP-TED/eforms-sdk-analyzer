@tedefo-1870
Feature: View templates - All EFX templates compile successfully
  TEDEFO-1870: Translate all EFX view-templates to XSL using EFX toolkit and verify no exceptions are thrown.
  Test files under under "src/test/resources/eforms-sdk-tests/efx"

  Scenario: The compilation of some EFX templates fails
    Given A "efx" folder with "" files
    When I execute EFX "templates" validation
    Then I should get 2 EFX validation errors
