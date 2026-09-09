@tedefo-5173
Feature: Schemas - Every eForms extension element allowed by the schemas is covered by a field or a node
  TEDEFO-5173: an element the eForms extension schemas allow, but which no field and no node
  covers, can appear in a notice while being invisible to the metadata.
  Reported as a warning: it may be a deliberate omission, and adding a field is a metadata decision.
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-5173"

  Scenario: Every element allowed by the schemas is covered
    Given A "tedefo-5173" folder with "valid" files
    When I execute schema validation
    Then I should get 0 schema validation warnings
    And I should get 0 schema validation errors

  Scenario: Four elements are not covered, one per way of reaching them
    # 1. under a node:              efac:AppealProcessingParty has a field for the Code
    #                               (BT-799-ReviewBody) but not for the Description
    # 2. under a node:              efac:TenderSubcontractingRequirements, same shape (BT-651-Lot)
    # 3. under an intermediate:     efac:NoticeSubType has no node of its own, it is reached only
    #                               through OPP-070-notice's two-step relative path, and its
    #                               efbc:SubTypeDescription is uncovered
    # 4. under the notice root:     efac:BusinessPartyGroup is referenced straight from
    #                               ContractNoticeType, outside ext:UBLExtensions, and is unmodelled
    #
    # Note what is deliberately NOT reported: efbc:GroupTypeCode and efbc:GroupType inside
    # efac:BusinessPartyGroup. Their parent has no node, so there is nowhere to hang them yet -
    # the parent is reported instead, and they surface once it is modelled.
    Given A "tedefo-5173" folder with "invalid" files
    When I execute schema validation
    Then I should get 4 schema validation warnings
    And I should get 0 schema validation errors
