@tedefo-5173
Feature: Schemas - Report XML elements not covered by any field or node
  TEDEFO-5173: an element the eForms extension schemas allow under a modelled node, but which
  no field and no node covers, can appear in a notice while being invisible to the metadata.
  Reported as a warning: it may be a deliberate omission, and adding a field is a metadata decision.
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-5173"

  Scenario: Every element allowed by the schemas is covered
    Given A "tedefo-5173" folder with "valid" files
    When I execute schema validation
    Then I should get 0 schema validation warnings
    And I should get 0 schema validation errors

  Scenario: Two free-text counterparts are not covered
    # efac:AppealProcessingParty allows a Code and a Description, only the Code has a field
    # (BT-799-ReviewBody); efac:TenderSubcontractingRequirements is the same case (BT-651-Lot).
    Given A "tedefo-5173" folder with "invalid" files
    When I execute schema validation
    Then I should get 2 schema validation warnings
    And I should get 0 schema validation errors
