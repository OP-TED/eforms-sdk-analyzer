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

  Scenario: Uncovered elements are reported, one per way of reaching them
    # Inside a parent the metadata reaches - the first check:
    #  1. under a node:             efac:AppealProcessingParty has a field for the Code
    #                              (BT-799-ReviewBody) but not for the Description
    #  2. under a node:             efac:TenderSubcontractingRequirements, same shape (BT-651-Lot)
    #  3. under an intermediate:    efac:NoticeSubType has no node of its own, it is reached only
    #                              through OPP-070-notice's two-step relative path, and its
    #                              efbc:SubTypeDescription is uncovered
    #  4. under the notice root:    efac:BusinessPartyGroup is referenced straight from
    #                              ContractNoticeType, outside ext:UBLExtensions, and is unmodelled
    #  5. under the extension:      the same efac:BusinessPartyGroup, at its second placement
    #
    # Inside a branch the metadata does not reach - the companion check, one finding per element
    # rather than per location:
    #  6. efbc:GroupTypeCode        several locations (both placements of efac:BusinessPartyGroup)
    #  7. efbc:GroupType            several locations, likewise
    #
    # Two further unmodelled branches, each reachable from one place only, so what is inside them is
    # reported as a single location. They also carry the two shapes the descent has to survive:
    #  8. efac:SelectionCriteria    the branch itself, from the first check
    #  9. efbc:CriterionDescription single location - and its being single is what proves the descent
    #                              stopped on efac:SubordinateCriterion, which is of the same type as
    #                              the aggregate that holds it. Following the type twice would report
    #                              the leaf at two locations instead, or not stop at all.
    # 10. efac:SubordinateCriterion single location
    # 11. efac:NoticeResult         the branch itself, from the first check
    # 12. efac:LotResult            single location, one level down
    # 13. efac:ReceivedSubmissionsStatistics  single location, two levels down - the last level the
    #                              descent goes to. efbc:StatisticsCode sits inside it, one level
    #                              deeper, and is deliberately absent from the count: the branch is
    #                              reported near its root, not exhaustively.
    Given A "tedefo-5173" folder with "invalid" files
    When I execute schema validation
    Then I should get 13 schema validation warnings
    And I should get 0 schema validation errors
    # The split matters as much as the total: were the descent to follow efac:SubordinateCriterion
    # back into its own type, efbc:CriterionDescription would move from single to several locations
    # without changing the number of findings.
    And I should get 4 schema validation warnings about "a single location"
    And I should get 2 schema validation warnings about "several locations"
    # Several nodes can stand for the same element, and the one with the shortest absolute XPath is
    # the placement a finding names. ND-BlankXpath stands for efext:EformsExtension with an empty
    # absolute XPath: shorter than any real one, so it must be dropped rather than win the comparison
    # and leave finding 5 pointing at "/efac:BusinessPartyGroup".
    And Schema validation should reference the XML element "/*/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:BusinessPartyGroup"
