@tedefo-1870
Feature: View templates - All EFX templates compile successfully
  TEDEFO-1870: Translate all EFX view-templates to XSL using EFX toolkit and verify no exceptions are thrown.
  Test files under under "src/test/resources/eforms-sdk-tests/tedefo-1870"

  Scenario Outline: The compilation of some EFX templates fails
    Given A "tedefo-1870" folder with "" files
    When I execute view templates validation
    Then I should get 2 template validation errors
