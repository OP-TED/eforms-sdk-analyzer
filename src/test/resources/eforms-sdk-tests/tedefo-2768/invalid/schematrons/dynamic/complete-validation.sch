<?xml version="1.0" encoding="utf-8" ?>
<!--File generated from metadata database-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

	<title>eForms schematron rules</title>
	
	<ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema" />
	<ns prefix="sch" uri="http://purl.oclc.org/dsdl/schematron" />
	<ns prefix="cbc" uri='urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2' />
	<ns prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" />
	<ns prefix="ext" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" />
	<ns prefix="efac" uri="http://data.europa.eu/p27/eforms-ubl-extension-aggregate-components/1" />
	<ns prefix="efext" uri="http://data.europa.eu/p27/eforms-ubl-extensions/1" />
	<ns prefix="efbc" uri="http://data.europa.eu/p27/eforms-ubl-extension-basic-components/1" />
	<ns prefix="can" uri="urn:oasis:names:specification:ubl:schema:xsd:ContractAwardNotice-2" />
	<ns prefix="cn" uri="urn:oasis:names:specification:ubl:schema:xsd:ContractNotice-2" />
	<ns prefix="pin" uri="urn:oasis:names:specification:ubl:schema:xsd:PriorInformationNotice-2" />
	<ns prefix="fn" uri="http://www.w3.org/2005/xpath-functions" />
	
	<let name="noticeSubType" value="/*/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:NoticeSubType/cbc:SubTypeCode/text()"/>

	<!-- True if the current notice is a change notice. -->
	<let name="isChangeNotice" value="boolean(/*/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:Changes/efbc:ChangedNoticeIdentifier)"/>

	<include href="config.sch"/>
	
	<phase id="eforms-1">
		<active pattern="EFORMS-validation-stage-1a" />
		<active pattern="EFORMS-validation-stage-1b-1" />
	</phase>
	<phase id="eforms-16BAD">
		<active pattern="EFORMS-validation-stage-1a" />
		<active pattern="EFORMS-validation-stage-1b-16" />
	</phase>

	<include href="validation-stage-1a.sch"/>
	<include href="validation-stage-1b-1.sch"/>
	<include href="validation-stage-1b-16.sch"/>

	<diagnostics>
		<diagnostic id="ND-AcceleratedProcedure" see="node:ND-AcceleratedProcedure">cac:ProcessJustification</diagnostic>
		<diagnostic id="ND-AwardingTerms" see="node:ND-AwardingTerms">cac:AwardingTerms</diagnostic>
		<diagnostic id="ND-AwardingTerms_BT-46-Lot" see="field:BT-46-Lot">cac:TechnicalCommitteePerson/cbc:FamilyName</diagnostic>
		<diagnostic id="ND-BusinessAddress" see="node:ND-BusinessAddress">cac:PostalAddress</diagnostic>
		<diagnostic id="ND-BusinessContact" see="node:ND-BusinessContact">cac:Contact</diagnostic>
		<diagnostic id="ND-BusinessParty" see="node:ND-BusinessParty">cac:BusinessParty</diagnostic>
		<diagnostic id="ND-BusinessParty_BT-501-Business-European" see="field:BT-501-Business-European">cac:PartyLegalEntity[cbc:CompanyID/@schemeName = 'EU']/cbc:CompanyID</diagnostic>
		<diagnostic id="ND-BusinessParty_BT-501-Business-National" see="field:BT-501-Business-National">cac:PartyLegalEntity[cbc:CompanyID/@schemeName = 'national']/cbc:CompanyID</diagnostic>
		<diagnostic id="ND-Buyer" see="node:ND-Buyer">cac:Party</diagnostic>
		<diagnostic id="ND-Buyer_OPT-300-Procedure-SProvider" see="field:OPT-300-Procedure-SProvider">cac:ServiceProviderParty/cac:Party/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-Change" see="node:ND-Change">efac:Change</diagnostic>
		<diagnostic id="ND-Company" see="node:ND-Company">efac:Company</diagnostic>
		<diagnostic id="ND-Company_BT-16-Organization-Company" see="field:BT-16-Organization-Company">cac:PostalAddress/cbc:Department</diagnostic>
		<diagnostic id="ND-Company_BT-501-Organization-Company" see="field:BT-501-Organization-Company">cac:PartyLegalEntity/cbc:CompanyID</diagnostic>
		<diagnostic id="ND-Company_BT-502-Organization-Company" see="field:BT-502-Organization-Company">cac:Contact/cbc:Name</diagnostic>
		<diagnostic id="ND-Company_BT-503-Organization-Company" see="field:BT-503-Organization-Company">cac:Contact/cbc:Telephone</diagnostic>
		<diagnostic id="ND-Company_BT-506-Organization-Company" see="field:BT-506-Organization-Company">cac:Contact/cbc:ElectronicMail</diagnostic>
		<diagnostic id="ND-Company_BT-507-Organization-Company" see="field:BT-507-Organization-Company">cac:PostalAddress/cbc:CountrySubentityCode</diagnostic>
		<diagnostic id="ND-Company_BT-510_a_-Organization-Company" see="field:BT-510(a)-Organization-Company">cac:PostalAddress/cbc:StreetName</diagnostic>
		<diagnostic id="ND-Company_BT-510_b_-Organization-Company" see="field:BT-510(b)-Organization-Company">cac:PostalAddress/cbc:AdditionalStreetName</diagnostic>
		<diagnostic id="ND-Company_BT-510_c_-Organization-Company" see="field:BT-510(c)-Organization-Company">cac:PostalAddress/cac:AddressLine/cbc:Line</diagnostic>
		<diagnostic id="ND-Company_BT-512-Organization-Company" see="field:BT-512-Organization-Company">cac:PostalAddress/cbc:PostalZone</diagnostic>
		<diagnostic id="ND-Company_BT-513-Organization-Company" see="field:BT-513-Organization-Company">cac:PostalAddress/cbc:CityName</diagnostic>
		<diagnostic id="ND-Company_BT-514-Organization-Company" see="field:BT-514-Organization-Company">cac:PostalAddress/cac:Country/cbc:IdentificationCode</diagnostic>
		<diagnostic id="ND-Company_BT-739-Organization-Company" see="field:BT-739-Organization-Company">cac:Contact/cbc:Telefax</diagnostic>
		<diagnostic id="ND-ConcessionRevenue" see="node:ND-ConcessionRevenue">efac:ConcessionRevenue</diagnostic>
		<diagnostic id="ND-ContractEUFunds" see="node:ND-ContractEUFunds">efac:Funding</diagnostic>
		<diagnostic id="ND-ContractModification" see="node:ND-ContractModification">efac:ContractModification</diagnostic>
		<diagnostic id="ND-ContractModification_BT-1501_s_-Contract" see="field:BT-1501(s)-Contract">efac:Change/efac:ChangedSection/efbc:ChangedSectionIdentifier</diagnostic>
		<diagnostic id="ND-ContractingParty" see="node:ND-ContractingParty">cac:ContractingParty</diagnostic>
		<diagnostic id="ND-CrossBorderLawUnpublish" see="node:ND-CrossBorderLawUnpublish">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-ExclusionGrounds" see="node:ND-ExclusionGrounds">cac:SpecificTendererRequirement</diagnostic>
		<diagnostic id="ND-ExecutionRequirements" see="node:ND-ExecutionRequirements">cac:ContractExecutionRequirement</diagnostic>
		<diagnostic id="ND-ExtendedDurationJustification" see="node:ND-ExtendedDurationJustification">efac:DurationJustification</diagnostic>
		<diagnostic id="ND-FA" see="node:ND-FA">cac:FrameworkAgreement</diagnostic>
		<diagnostic id="ND-Funding" see="node:ND-Funding">efac:Funding</diagnostic>
		<diagnostic id="ND-GazetteReference" see="node:ND-GazetteReference">cac:AdditionalDocumentReference</diagnostic>
		<diagnostic id="ND-GroupComposition" see="node:ND-GroupComposition">cac:LotsGroup</diagnostic>
		<diagnostic id="ND-GroupComposition_BT-1375-Procedure" see="field:BT-1375-Procedure">cac:ProcurementProjectLotReference/cbc:ID[@schemeName='Lot']</diagnostic>
		<diagnostic id="ND-InterestExpressionReceptionPeriod" see="node:ND-InterestExpressionReceptionPeriod">efac:InterestExpressionReceptionPeriod</diagnostic>
		<diagnostic id="ND-LocalEntity" see="node:ND-LocalEntity">cac:PartyLegalEntity</diagnostic>
		<diagnostic id="ND-LotAwardCriteria" see="node:ND-LotAwardCriteria">cac:AwardingCriterion</diagnostic>
		<diagnostic id="ND-LotAwardCriteria_BT-539-Lot" see="field:BT-539-Lot">cac:SubordinateAwardingCriterion/cbc:AwardingCriterionTypeCode[@listName='award-criterion-type']</diagnostic>
		<diagnostic id="ND-LotAwardCriterionFixNumberUnpublish" see="node:ND-LotAwardCriterionFixNumberUnpublish">efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-LotAwardCriterionNumberComplicatedUnpublish" see="node:ND-LotAwardCriterionNumberComplicatedUnpublish">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-LotAwardCriterionParameters_BT-541-Lot-FixedNumber" see="field:BT-541-Lot-FixedNumber">efac:AwardCriterionParameter[efbc:ParameterCode/@listName='number-fixed']/efbc:ParameterNumeric</diagnostic>
		<diagnostic id="ND-LotAwardCriterionParameters_BT-541-Lot-ThresholdNumber" see="field:BT-541-Lot-ThresholdNumber">efac:AwardCriterionParameter[efbc:ParameterCode/@listName='number-threshold']/efbc:ParameterNumeric</diagnostic>
		<diagnostic id="ND-LotAwardCriterionParameters_BT-541-Lot-WeightNumber" see="field:BT-541-Lot-WeightNumber">efac:AwardCriterionParameter[efbc:ParameterCode/@listName='number-weight']/efbc:ParameterNumeric</diagnostic>
		<diagnostic id="ND-LotAwardCriterionTypeUnpublish" see="node:ND-LotAwardCriterionTypeUnpublish">efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-LotDistribution" see="node:ND-LotDistribution">cac:LotDistribution</diagnostic>
		<diagnostic id="ND-LotDistribution_BT-330-Procedure" see="field:BT-330-Procedure">cac:LotsGroup/cbc:LotsGroupID</diagnostic>
		<diagnostic id="ND-LotDuration" see="node:ND-LotDuration">cac:PlannedPeriod</diagnostic>
		<diagnostic id="ND-LotEmploymentLegislation" see="node:ND-LotEmploymentLegislation">cac:EmploymentLegislationDocumentReference</diagnostic>
		<diagnostic id="ND-LotEnvironmentalLegislation" see="node:ND-LotEnvironmentalLegislation">cac:EnvironmentalLegislationDocumentReference</diagnostic>
		<diagnostic id="ND-LotFiscalLegislation" see="node:ND-LotFiscalLegislation">cac:FiscalLegislationDocumentReference</diagnostic>
		<diagnostic id="ND-LotPlacePerformance_BT-5071-Lot" see="field:BT-5071-Lot">cac:Address/cbc:CountrySubentityCode</diagnostic>
		<diagnostic id="ND-LotPlacePerformance_BT-5101_a_-Lot" see="field:BT-5101(a)-Lot">cac:Address/cbc:StreetName</diagnostic>
		<diagnostic id="ND-LotPlacePerformance_BT-5101_b_-Lot" see="field:BT-5101(b)-Lot">cac:Address/cbc:AdditionalStreetName</diagnostic>
		<diagnostic id="ND-LotPlacePerformance_BT-5101_c_-Lot" see="field:BT-5101(c)-Lot">cac:Address/cac:AddressLine/cbc:Line</diagnostic>
		<diagnostic id="ND-LotPlacePerformance_BT-5121-Lot" see="field:BT-5121-Lot">cac:Address/cbc:PostalZone</diagnostic>
		<diagnostic id="ND-LotPlacePerformance_BT-5131-Lot" see="field:BT-5131-Lot">cac:Address/cbc:CityName</diagnostic>
		<diagnostic id="ND-LotPlacePerformance_BT-5141-Lot" see="field:BT-5141-Lot">cac:Address/cac:Country/cbc:IdentificationCode</diagnostic>
		<diagnostic id="ND-LotPlacePerformance_BT-727-Lot" see="field:BT-727-Lot">cac:Address/cbc:Region</diagnostic>
		<diagnostic id="ND-LotPreviousPlanning" see="node:ND-LotPreviousPlanning">cac:NoticeDocumentReference</diagnostic>
		<diagnostic id="ND-LotProcurementDocument" see="node:ND-LotProcurementDocument">cac:CallForTendersDocumentReference</diagnostic>
		<diagnostic id="ND-LotProcurementDocument_BT-708-Lot" see="field:BT-708-Lot">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:OfficialLanguages/cac:Language/cbc:ID</diagnostic>
		<diagnostic id="ND-LotProcurementDocument_BT-737-Lot" see="field:BT-737-Lot">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:NonOfficialLanguages/cac:Language/cbc:ID</diagnostic>
		<diagnostic id="ND-LotProcurementScope" see="node:ND-LotProcurementScope">cac:ProcurementProject</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-06-Lot" see="field:BT-06-Lot">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='strategic-procurement']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-262-Lot" see="field:BT-262-Lot">cac:MainCommodityClassification/cbc:ItemClassificationCode</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-263-Lot" see="field:BT-263-Lot">cac:AdditionalCommodityClassification/cbc:ItemClassificationCode</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-271-Lot" see="field:BT-271-Lot">cac:RequestedTenderTotal/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efbc:FrameworkMaximumAmount</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-531-Lot" see="field:BT-531-Lot">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='contract-nature']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-754-Lot" see="field:BT-754-Lot">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='accessibility']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-774-Lot" see="field:BT-774-Lot">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='environmental-impact']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-775-Lot" see="field:BT-775-Lot">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='social-objective']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-776-Lot" see="field:BT-776-Lot">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='innovative-acquisition']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-LotProcurementScope_BT-805-Lot" see="field:BT-805-Lot">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='gpp-criteria']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-LotReservedParticipation" see="node:ND-LotReservedParticipation">cac:TendererQualificationRequest</diagnostic>
		<diagnostic id="ND-LotReservedParticipation_BT-71-Lot" see="field:BT-71-Lot">cac:SpecificTendererRequirement[cbc:TendererRequirementTypeCode/@listName='reserved-procurement']/cbc:TendererRequirementTypeCode</diagnostic>
		<diagnostic id="ND-LotResult" see="node:ND-LotResult">efac:LotResult</diagnostic>
		<diagnostic id="ND-LotResultFAValues" see="node:ND-LotResultFAValues">efac:FrameworkAgreementValues</diagnostic>
		<diagnostic id="ND-LotResult_BT-636-LotResult" see="field:BT-636-LotResult">efac:AppealRequestsStatistics[efbc:StatisticsCode/@listName='irregularity-type']/efbc:StatisticsCode</diagnostic>
		<diagnostic id="ND-LotResult_BT-712_a_-LotResult" see="field:BT-712(a)-LotResult">efac:AppealRequestsStatistics[efbc:StatisticsCode/@listName='review-type']/efbc:StatisticsCode</diagnostic>
		<diagnostic id="ND-LotResult_BT-759-LotResult" see="field:BT-759-LotResult">efac:ReceivedSubmissionsStatistics/efbc:StatisticsNumeric</diagnostic>
		<diagnostic id="ND-LotResult_BT-760-LotResult" see="field:BT-760-LotResult">efac:ReceivedSubmissionsStatistics/efbc:StatisticsCode</diagnostic>
		<diagnostic id="ND-LotResult_OPT-301-LotResult-Financing" see="field:OPT-301-LotResult-Financing">cac:FinancingParty/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-LotResult_OPT-301-LotResult-Paying" see="field:OPT-301-LotResult-Paying">cac:PayerParty/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-LotResult_OPT-315-LotResult" see="field:OPT-315-LotResult">efac:SettledContract/cbc:ID</diagnostic>
		<diagnostic id="ND-LotResult_OPT-320-LotResult" see="field:OPT-320-LotResult">efac:LotTender/cbc:ID</diagnostic>
		<diagnostic id="ND-LotReviewTerms" see="node:ND-LotReviewTerms">cac:AppealTerms</diagnostic>
		<diagnostic id="ND-LotReviewTerms_BT-99-Lot" see="field:BT-99-Lot">cac:PresentationPeriod/cbc:Description</diagnostic>
		<diagnostic id="ND-LotTender" see="node:ND-LotTender">efac:LotTender</diagnostic>
		<diagnostic id="ND-LotTenderOriginCountry" see="node:ND-LotTenderOriginCountry">efac:Origin</diagnostic>
		<diagnostic id="ND-LotTender_BT-191-Tender" see="field:BT-191-Tender">efac:Origin/efbc:AreaCode</diagnostic>
		<diagnostic id="ND-LotTender_BT-779-Tender" see="field:BT-779-Tender">efac:AggregatedAmounts/cbc:PaidAmount</diagnostic>
		<diagnostic id="ND-LotTender_BT-780-Tender" see="field:BT-780-Tender">efac:AggregatedAmounts/efbc:PaidAmountDescription</diagnostic>
		<diagnostic id="ND-LotTender_BT-782-Tender" see="field:BT-782-Tender">efac:AggregatedAmounts/efbc:PenaltiesAmount</diagnostic>
		<diagnostic id="ND-LotTender_OPP-030-Tender" see="field:OPP-030-Tender">efac:ContractTerm[not(efbc:TermCode/text()='all-rev-tic')][efbc:TermCode/@listName='contract-detail']/efbc:TermCode</diagnostic>
		<diagnostic id="ND-LotTender_OPP-033-Tender" see="field:OPP-033-Tender">efac:ContractTerm[efbc:TermCode/@listName='rewards-penalties']/efbc:TermCode</diagnostic>
		<diagnostic id="ND-LotTenderingProcess" see="node:ND-LotTenderingProcess">cac:TenderingProcess</diagnostic>
		<diagnostic id="ND-LotTenderingProcessExtension" see="node:ND-LotTenderingProcessExtension">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-1251-Lot" see="field:BT-1251-Lot">cac:NoticeDocumentReference/cbc:ReferencedDocumentInternalAddress</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-1311_d_-Lot" see="field:BT-1311(d)-Lot">cac:ParticipationRequestReceptionPeriod/cbc:EndDate</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-1311_t_-Lot" see="field:BT-1311(t)-Lot">cac:ParticipationRequestReceptionPeriod/cbc:EndTime</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-131_d_-Lot" see="field:BT-131(d)-Lot">cac:TenderSubmissionDeadlinePeriod/cbc:EndDate</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-131_t_-Lot" see="field:BT-131(t)-Lot">cac:TenderSubmissionDeadlinePeriod/cbc:EndTime</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-132_d_-Lot" see="field:BT-132(d)-Lot">cac:OpenTenderEvent/cbc:OccurrenceDate</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-13_d_-Lot" see="field:BT-13(d)-Lot">cac:AdditionalInformationRequestPeriod/cbc:EndDate</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-13_t_-Lot" see="field:BT-13(t)-Lot">cac:AdditionalInformationRequestPeriod/cbc:EndTime</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-765-Lot" see="field:BT-765-Lot">cac:ContractingSystem[cbc:ContractingSystemTypeCode/@listName='framework-agreement']/cbc:ContractingSystemTypeCode</diagnostic>
		<diagnostic id="ND-LotTenderingProcess_BT-766-Lot" see="field:BT-766-Lot">cac:ContractingSystem[cbc:ContractingSystemTypeCode/@listName='dps-usage']/cbc:ContractingSystemTypeCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms" see="node:ND-LotTenderingTerms">cac:TenderingTerms</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-18-Lot" see="field:BT-18-Lot">cac:TenderRecipientParty/cbc:EndpointID</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-65-Lot" see="field:BT-65-Lot">cac:AllowedSubcontractTerms[cbc:SubcontractingConditionsCode/@listName='subcontracting-obligation']/cbc:SubcontractingConditionsCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-736-Lot" see="field:BT-736-Lot">cac:ContractExecutionRequirement[cbc:ExecutionRequirementCode/@listName='reserved-execution']/cbc:ExecutionRequirementCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-743-Lot" see="field:BT-743-Lot">cac:ContractExecutionRequirement[cbc:ExecutionRequirementCode/@listName='einvoicing']/cbc:ExecutionRequirementCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-744-Lot" see="field:BT-744-Lot">cac:ContractExecutionRequirement[cbc:ExecutionRequirementCode/@listName='esignature-submission']/cbc:ExecutionRequirementCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-75-Lot" see="field:BT-75-Lot">cac:RequiredFinancialGuarantee/cbc:Description</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-751-Lot" see="field:BT-751-Lot">cac:RequiredFinancialGuarantee/cbc:GuaranteeTypeCode[@listName='tender-guarantee-required']</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-76-Lot" see="field:BT-76-Lot">cac:TendererQualificationRequest[not(cac:SpecificTendererRequirement)]/cbc:CompanyLegalForm</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-761-Lot" see="field:BT-761-Lot">cac:TendererQualificationRequest[not(cac:SpecificTendererRequirement)]/cbc:CompanyLegalFormCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-764-Lot" see="field:BT-764-Lot">cac:ContractExecutionRequirement[cbc:ExecutionRequirementCode/@listName='ecatalog-submission']/cbc:ExecutionRequirementCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-801-Lot" see="field:BT-801-Lot">cac:ContractExecutionRequirement[cbc:ExecutionRequirementCode/@listName='nda']/cbc:ExecutionRequirementCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_BT-97-Lot" see="field:BT-97-Lot">cac:Language/cbc:ID</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_OPT-060-Lot" see="field:OPT-060-Lot">cac:ContractExecutionRequirement[cbc:ExecutionRequirementCode/@listName='conditions']/cbc:ExecutionRequirementCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_OPT-071-Lot" see="field:OPT-071-Lot">cac:ContractExecutionRequirement[cbc:ExecutionRequirementCode/@listName='customer-service']/cbc:ExecutionRequirementCode</diagnostic>
		<diagnostic id="ND-LotTenderingTerms_OPT-301-Lot-TenderReceipt" see="field:OPT-301-Lot-TenderReceipt">cac:TenderRecipientParty/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-LotValueEstimate" see="node:ND-LotValueEstimate">cac:RequestedTenderTotal</diagnostic>
		<diagnostic id="ND-LotsGroupAwardCriteria_BT-539-LotsGroup" see="field:BT-539-LotsGroup">cac:SubordinateAwardingCriterion/cbc:AwardingCriterionTypeCode[@listName='award-criterion-type']</diagnostic>
		<diagnostic id="ND-LotsGroupAwardCriterionFixNumberUnpublish" see="node:ND-LotsGroupAwardCriterionFixNumberUnpublish">efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-LotsGroupAwardCriterionNumberComplicatedUnpublish" see="node:ND-LotsGroupAwardCriterionNumberComplicatedUnpublish">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-LotsGroupAwardCriterionParameters_BT-541-LotsGroup-FixedNumber" see="field:BT-541-LotsGroup-FixedNumber">efac:AwardCriterionParameter[efbc:ParameterCode/@listName='number-fixed']/efbc:ParameterNumeric</diagnostic>
		<diagnostic id="ND-LotsGroupAwardCriterionParameters_BT-541-LotsGroup-ThresholdNumber" see="field:BT-541-LotsGroup-ThresholdNumber">efac:AwardCriterionParameter[efbc:ParameterCode/@listName='number-threshold']/efbc:ParameterNumeric</diagnostic>
		<diagnostic id="ND-LotsGroupAwardCriterionParameters_BT-541-LotsGroup-WeightNumber" see="field:BT-541-LotsGroup-WeightNumber">efac:AwardCriterionParameter[efbc:ParameterCode/@listName='number-weight']/efbc:ParameterNumeric</diagnostic>
		<diagnostic id="ND-LotsGroupAwardCriterionTypeUnpublish" see="node:ND-LotsGroupAwardCriterionTypeUnpublish">efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-LotsGroupAwardingTerms" see="node:ND-LotsGroupAwardingTerms">cac:TenderingTerms/cac:AwardingTerms</diagnostic>
		<diagnostic id="ND-LotsGroupFA" see="node:ND-LotsGroupFA">cac:TenderingProcess/cac:FrameworkAgreement</diagnostic>
		<diagnostic id="ND-LotsGroupProcurementScope_BT-271-LotsGroup" see="field:BT-271-LotsGroup">cac:RequestedTenderTotal/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efbc:FrameworkMaximumAmount</diagnostic>
		<diagnostic id="ND-LotsGroupValueEstimate" see="node:ND-LotsGroupValueEstimate">cac:RequestedTenderTotal</diagnostic>
		<diagnostic id="ND-Modification" see="node:ND-Modification">efac:Change</diagnostic>
		<diagnostic id="ND-ModificationReason" see="node:ND-ModificationReason">efac:ChangeReason</diagnostic>
		<diagnostic id="ND-ModifiedSection" see="node:ND-ModifiedSection">efac:ChangedSection</diagnostic>
		<diagnostic id="ND-NonEsubmission" see="node:ND-NonEsubmission">cac:ProcessJustification</diagnostic>
		<diagnostic id="ND-NonUBLTenderingTerms" see="node:ND-NonUBLTenderingTerms">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension</diagnostic>
		<diagnostic id="ND-NonUBLTenderingTerms_BT-5010-Lot" see="field:BT-5010-Lot">efac:Funding/efbc:FinancingIdentifier</diagnostic>
		<diagnostic id="ND-NonUBLTenderingTerms_BT-651-Lot" see="field:BT-651-Lot">efac:TenderSubcontractingRequirements/efbc:TenderSubcontractingRequirementsCode</diagnostic>
		<diagnostic id="ND-NonUBLTenderingTerms_BT-7220-Lot" see="field:BT-7220-Lot">efac:Funding/cbc:FundingProgramCode</diagnostic>
		<diagnostic id="ND-NonUBLTenderingTerms_BT-747-Lot" see="field:BT-747-Lot">efac:SelectionCriteria/cbc:CriterionTypeCode[@listName='selection-criterion']</diagnostic>
		<diagnostic id="ND-NotAwardedReasonUnpublish" see="node:ND-NotAwardedReasonUnpublish">efac:DecisionReason/efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-NoticeApproximateValueUnpublish" see="node:ND-NoticeApproximateValueUnpublish">efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-NoticeResult" see="node:ND-NoticeResult">efac:NoticeResult</diagnostic>
		<diagnostic id="ND-NoticeResultGroupFA" see="node:ND-NoticeResultGroupFA">efac:GroupFramework</diagnostic>
		<diagnostic id="ND-OperationType" see="node:ND-OperationType">efac:NoticePurpose</diagnostic>
		<diagnostic id="ND-OptionsAndRenewals" see="node:ND-OptionsAndRenewals">cac:ContractExtension</diagnostic>
		<diagnostic id="ND-OptionsAndRenewals_BT-57-Lot" see="field:BT-57-Lot">cac:Renewal/cac:Period/cbc:Description</diagnostic>
		<diagnostic id="ND-Organization" see="node:ND-Organization">efac:Organization</diagnostic>
		<diagnostic id="ND-Organization_OPT-201-Organization-TouchPoint" see="field:OPT-201-Organization-TouchPoint">efac:TouchPoint/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-Organization_OPT-302-Organization" see="field:OPT-302-Organization">efac:UltimateBeneficialOwner/cbc:ID</diagnostic>
		<diagnostic id="ND-Organizations" see="node:ND-Organizations">efac:Organizations</diagnostic>
		<diagnostic id="ND-Organizations_OPT-202-UBO" see="field:OPT-202-UBO">efac:UltimateBeneficialOwner/cbc:ID</diagnostic>
		<diagnostic id="ND-OtherContractExecutionConditions" see="node:ND-OtherContractExecutionConditions">efac:ContractTerm</diagnostic>
		<diagnostic id="ND-PMCAnswersDeadline" see="node:ND-PMCAnswersDeadline">efac:AnswerReceptionPeriod</diagnostic>
		<diagnostic id="ND-Part" see="node:ND-Part">cac:ProcurementProjectLot[cbc:ID/@schemeName='Part']</diagnostic>
		<diagnostic id="ND-PartEmploymentLegislation" see="node:ND-PartEmploymentLegislation">cac:EmploymentLegislationDocumentReference</diagnostic>
		<diagnostic id="ND-PartEnvironmentalLegislation" see="node:ND-PartEnvironmentalLegislation">cac:EnvironmentalLegislationDocumentReference</diagnostic>
		<diagnostic id="ND-PartFiscalLegislation" see="node:ND-PartFiscalLegislation">cac:FiscalLegislationDocumentReference</diagnostic>
		<diagnostic id="ND-PartPlacePerformance_BT-5071-Part" see="field:BT-5071-Part">cac:Address/cbc:CountrySubentityCode</diagnostic>
		<diagnostic id="ND-PartPlacePerformance_BT-5101_a_-Part" see="field:BT-5101(a)-Part">cac:Address/cbc:StreetName</diagnostic>
		<diagnostic id="ND-PartPlacePerformance_BT-5101_b_-Part" see="field:BT-5101(b)-Part">cac:Address/cbc:AdditionalStreetName</diagnostic>
		<diagnostic id="ND-PartPlacePerformance_BT-5101_c_-Part" see="field:BT-5101(c)-Part">cac:Address/cac:AddressLine/cbc:Line</diagnostic>
		<diagnostic id="ND-PartPlacePerformance_BT-5121-Part" see="field:BT-5121-Part">cac:Address/cbc:PostalZone</diagnostic>
		<diagnostic id="ND-PartPlacePerformance_BT-5131-Part" see="field:BT-5131-Part">cac:Address/cbc:CityName</diagnostic>
		<diagnostic id="ND-PartPlacePerformance_BT-5141-Part" see="field:BT-5141-Part">cac:Address/cac:Country/cbc:IdentificationCode</diagnostic>
		<diagnostic id="ND-PartPlacePerformance_BT-727-Part" see="field:BT-727-Part">cac:Address/cbc:Region</diagnostic>
		<diagnostic id="ND-PartProcurementDocument_BT-708-Part" see="field:BT-708-Part">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:OfficialLanguages/cac:Language/cbc:ID</diagnostic>
		<diagnostic id="ND-PartProcurementDocument_BT-737-Part" see="field:BT-737-Part">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:NonOfficialLanguages/cac:Language/cbc:ID</diagnostic>
		<diagnostic id="ND-PartProcurementScope" see="node:ND-PartProcurementScope">cac:ProcurementProject</diagnostic>
		<diagnostic id="ND-PartProcurementScope_BT-262-Part" see="field:BT-262-Part">cac:MainCommodityClassification/cbc:ItemClassificationCode</diagnostic>
		<diagnostic id="ND-PartProcurementScope_BT-263-Part" see="field:BT-263-Part">cac:AdditionalCommodityClassification/cbc:ItemClassificationCode</diagnostic>
		<diagnostic id="ND-PartProcurementScope_BT-531-Part" see="field:BT-531-Part">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='contract-nature']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-PartReservedParticipation_BT-71-Part" see="field:BT-71-Part">cac:SpecificTendererRequirement[cbc:TendererRequirementTypeCode/@listName='reserved-procurement']/cbc:TendererRequirementTypeCode</diagnostic>
		<diagnostic id="ND-PartReviewTerms" see="node:ND-PartReviewTerms">cac:AppealTerms</diagnostic>
		<diagnostic id="ND-PartTenderingProcess_BT-1251-Part" see="field:BT-1251-Part">cac:NoticeDocumentReference/cbc:ReferencedDocumentInternalAddress</diagnostic>
		<diagnostic id="ND-PartTenderingProcess_BT-13_d_-Part" see="field:BT-13(d)-Part">cac:AdditionalInformationRequestPeriod/cbc:EndDate</diagnostic>
		<diagnostic id="ND-PartTenderingProcess_BT-13_t_-Part" see="field:BT-13(t)-Part">cac:AdditionalInformationRequestPeriod/cbc:EndTime</diagnostic>
		<diagnostic id="ND-PartTenderingProcess_BT-765-Part" see="field:BT-765-Part">cac:ContractingSystem[cbc:ContractingSystemTypeCode/@listName='framework-agreement']/cbc:ContractingSystemTypeCode</diagnostic>
		<diagnostic id="ND-PartTenderingProcess_BT-766-Part" see="field:BT-766-Part">cac:ContractingSystem[cbc:ContractingSystemTypeCode/@listName='dps-usage']/cbc:ContractingSystemTypeCode</diagnostic>
		<diagnostic id="ND-PartTenderingTerms" see="node:ND-PartTenderingTerms">cac:TenderingTerms</diagnostic>
		<diagnostic id="ND-PartTenderingTerms_BT-736-Part" see="field:BT-736-Part">cac:ContractExecutionRequirement[cbc:ExecutionRequirementCode/@listName='reserved-execution']/cbc:ExecutionRequirementCode</diagnostic>
		<diagnostic id="ND-Participants" see="node:ND-Participants">cac:EconomicOperatorShortList</diagnostic>
		<diagnostic id="ND-Participants_BT-47-Lot" see="field:BT-47-Lot">cac:PreSelectedParty/cac:PartyName/cbc:Name</diagnostic>
		<diagnostic id="ND-PaymentTerms" see="node:ND-PaymentTerms">cac:PaymentTerms</diagnostic>
		<diagnostic id="ND-PostAwardProcess" see="node:ND-PostAwardProcess">cac:PostAwardProcess</diagnostic>
		<diagnostic id="ND-Prize" see="node:ND-Prize">cac:Prize</diagnostic>
		<diagnostic id="ND-ProcedureAcceleratedUnpublish" see="node:ND-ProcedureAcceleratedUnpublish">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-ProcedurePlacePerformanceAdditionalInformation" see="node:ND-ProcedurePlacePerformanceAdditionalInformation">cac:RealizedLocation</diagnostic>
		<diagnostic id="ND-ProcedureProcurementScope" see="node:ND-ProcedureProcurementScope">cac:ProcurementProject</diagnostic>
		<diagnostic id="ND-ProcedureProcurementScope_BT-262-Procedure" see="field:BT-262-Procedure">cac:MainCommodityClassification/cbc:ItemClassificationCode</diagnostic>
		<diagnostic id="ND-ProcedureProcurementScope_BT-263-Procedure" see="field:BT-263-Procedure">cac:AdditionalCommodityClassification/cbc:ItemClassificationCode</diagnostic>
		<diagnostic id="ND-ProcedureProcurementScope_BT-271-Procedure" see="field:BT-271-Procedure">cac:RequestedTenderTotal/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efbc:FrameworkMaximumAmount</diagnostic>
		<diagnostic id="ND-ProcedureProcurementScope_BT-531-Procedure" see="field:BT-531-Procedure">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='contract-nature']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-ProcedureProcurementScope_OPP-040-Procedure" see="field:OPP-040-Procedure">cac:ProcurementAdditionalType[cbc:ProcurementTypeCode/@listName='transport-service']/cbc:ProcurementTypeCode</diagnostic>
		<diagnostic id="ND-ProcedureTenderingProcess" see="node:ND-ProcedureTenderingProcess">cac:TenderingProcess</diagnostic>
		<diagnostic id="ND-ProcedureTenderingProcess_OPP-090-Procedure" see="field:OPP-090-Procedure">cac:NoticeDocumentReference/cbc:ID</diagnostic>
		<diagnostic id="ND-ProcedureTerms" see="node:ND-ProcedureTerms">cac:TenderingTerms</diagnostic>
		<diagnostic id="ND-ProcedureTerms_BT-01_c_-Procedure" see="field:BT-01(c)-Procedure">cac:ProcurementLegislationDocumentReference[not(cbc:ID/text()=('CrossBorderLaw','LocalLegalBasis'))]/cbc:ID</diagnostic>
		<diagnostic id="ND-ProcedureTerms_BT-01_e_-Procedure" see="field:BT-01(e)-Procedure">cac:ProcurementLegislationDocumentReference[cbc:ID/text()='LocalLegalBasis']/cbc:ID</diagnostic>
		<diagnostic id="ND-ProcedureTerms_BT-09_a_-Procedure" see="field:BT-09(a)-Procedure">cac:ProcurementLegislationDocumentReference[cbc:ID/text()='CrossBorderLaw']/cbc:ID</diagnostic>
		<diagnostic id="ND-ProcedureTypeUnpublish" see="node:ND-ProcedureTypeUnpublish">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension/efac:FieldsPrivacy</diagnostic>
		<diagnostic id="ND-ProcedureValueEstimate" see="node:ND-ProcedureValueEstimate">cac:RequestedTenderTotal</diagnostic>
		<diagnostic id="ND-PublicOpening" see="node:ND-PublicOpening">cac:OpenTenderEvent</diagnostic>
		<diagnostic id="ND-PublicOpening_BT-133-Lot" see="field:BT-133-Lot">cac:OccurenceLocation/cbc:Description</diagnostic>
		<diagnostic id="ND-ReceivedSubmissions" see="node:ND-ReceivedSubmissions">efac:ReceivedSubmissionsStatistics</diagnostic>
		<diagnostic id="ND-ReviewRequests" see="node:ND-ReviewRequests">efac:AppealsInformation</diagnostic>
		<diagnostic id="ND-ReviewRequestsStatistics" see="node:ND-ReviewRequestsStatistics">efac:AppealRequestsStatistics</diagnostic>
		<diagnostic id="ND-ReviewStatus_BT-786-Review" see="field:BT-786-Review">efac:AppealedItem/cbc:ID</diagnostic>
		<diagnostic id="ND-ReviewStatus_BT-790-Review" see="field:BT-790-Review">efac:AppealDecision/efbc:DecisionTypeCode</diagnostic>
		<diagnostic id="ND-ReviewStatus_BT-791-Review" see="field:BT-791-Review">efac:AppealIrregularity/efbc:IrregularityTypeCode</diagnostic>
		<diagnostic id="ND-ReviewStatus_BT-792-Review" see="field:BT-792-Review">efac:AppealRemedy/efbc:RemedyTypeCode</diagnostic>
		<diagnostic id="ND-ReviewStatus_BT-799-ReviewBody" see="field:BT-799-ReviewBody">efac:AppealProcessingParty/efbc:AppealProcessingPartyTypeCode</diagnostic>
		<diagnostic id="ND-ReviewStatus_OPT-091-ReviewReq" see="field:OPT-091-ReviewReq">efac:AppealingParty/efbc:AppealingPartyTypeCode</diagnostic>
		<diagnostic id="ND-ReviewStatus_OPT-092-ReviewBody" see="field:OPT-092-ReviewBody">efac:AppealProcessingParty/efbc:AppealProcessingPartyTypeDescription</diagnostic>
		<diagnostic id="ND-ReviewStatus_OPT-301-ReviewBody" see="field:OPT-301-ReviewBody">efac:AppealProcessingParty/cac:Party/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-ReviewStatus_OPT-301-ReviewReq" see="field:OPT-301-ReviewReq">efac:AppealingParty/cac:Party/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-RootExtension" see="node:ND-RootExtension">ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/efext:EformsExtension</diagnostic>
		<diagnostic id="ND-RootExtension_OPP-010-notice" see="field:OPP-010-notice">efac:Publication/efbc:NoticePublicationID[@schemeName='ojs-notice-id']</diagnostic>
		<diagnostic id="ND-RootExtension_OPP-011-notice" see="field:OPP-011-notice">efac:Publication/efbc:GazetteID[@schemeName='ojs-id']</diagnostic>
		<diagnostic id="ND-RootExtension_OPP-012-notice" see="field:OPP-012-notice">efac:Publication/efbc:PublicationDate</diagnostic>
		<diagnostic id="ND-Root_OPP-105-Business" see="field:OPP-105-Business">cac:BusinessCapability/cbc:CapabilityTypeCode</diagnostic>
		<diagnostic id="ND-Root_OPT-300-Procedure-Buyer" see="field:OPT-300-Procedure-Buyer">cac:ContractingParty/cac:Party/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-SecondStage" see="node:ND-SecondStage">cac:EconomicOperatorShortList</diagnostic>
		<diagnostic id="ND-SecondStageWeightCriterionParameter" see="node:ND-SecondStageWeightCriterionParameter">efac:CriterionParameter</diagnostic>
		<diagnostic id="ND-SecurityClearanceTerms" see="node:ND-SecurityClearanceTerms">cac:SecurityClearanceTerm</diagnostic>
		<diagnostic id="ND-SelectionCriteria" see="node:ND-SelectionCriteria">efac:SelectionCriteria</diagnostic>
		<diagnostic id="ND-SelectionCriteria_BT-752-Lot-ThresholdNumber" see="field:BT-752-Lot-ThresholdNumber">efac:CriterionParameter[efbc:ParameterCode/@listName='number-threshold']/efbc:ParameterNumeric</diagnostic>
		<diagnostic id="ND-SelectionCriteria_BT-752-Lot-WeightNumber" see="field:BT-752-Lot-WeightNumber">efac:CriterionParameter[efbc:ParameterCode/@listName='number-weight']/efbc:ParameterNumeric</diagnostic>
		<diagnostic id="ND-SenderContact" see="node:ND-SenderContact">cac:SenderParty/cac:Contact</diagnostic>
		<diagnostic id="ND-SettledContract" see="node:ND-SettledContract">efac:SettledContract</diagnostic>
		<diagnostic id="ND-SettledContract_BT-3202-Contract" see="field:BT-3202-Contract">efac:LotTender/cbc:ID</diagnostic>
		<diagnostic id="ND-SettledContract_BT-5011-Contract" see="field:BT-5011-Contract">efac:Funding/efbc:FinancingIdentifier</diagnostic>
		<diagnostic id="ND-SettledContract_BT-722-Contract" see="field:BT-722-Contract">efac:Funding/cbc:FundingProgramCode</diagnostic>
		<diagnostic id="ND-SettledContract_OPT-300-Contract-Signatory" see="field:OPT-300-Contract-Signatory">cac:SignatoryParty/cac:PartyIdentification/cbc:ID</diagnostic>
		<diagnostic id="ND-StrategicProcurementLot" see="node:ND-StrategicProcurementLot">efac:StrategicProcurement</diagnostic>
		<diagnostic id="ND-StrategicProcurementLotResult" see="node:ND-StrategicProcurementLotResult">efac:StrategicProcurement</diagnostic>
		<diagnostic id="ND-SubContractor_OPT-301-Tenderer-MainCont" see="field:OPT-301-Tenderer-MainCont">efac:MainContractor/cbc:ID</diagnostic>
		<diagnostic id="ND-SubcontractedContract" see="node:ND-SubcontractedContract">efac:SubcontractingTerm</diagnostic>
		<diagnostic id="ND-SubcontractingObligation" see="node:ND-SubcontractingObligation">cac:AllowedSubcontractTerms</diagnostic>
		<diagnostic id="ND-TendererQualificationRequest" see="node:ND-TendererQualificationRequest">cac:TendererQualificationRequest</diagnostic>
		<diagnostic id="ND-TenderingParty_OPT-300-Tenderer" see="field:OPT-300-Tenderer">efac:Tenderer/cbc:ID</diagnostic>
		<diagnostic id="ND-TenderingParty_OPT-301-Tenderer-SubCont" see="field:OPT-301-Tenderer-SubCont">efac:SubContractor/cbc:ID</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-16-Organization-TouchPoint" see="field:BT-16-Organization-TouchPoint">cac:PostalAddress/cbc:Department</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-502-Organization-TouchPoint" see="field:BT-502-Organization-TouchPoint">cac:Contact/cbc:Name</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-503-Organization-TouchPoint" see="field:BT-503-Organization-TouchPoint">cac:Contact/cbc:Telephone</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-506-Organization-TouchPoint" see="field:BT-506-Organization-TouchPoint">cac:Contact/cbc:ElectronicMail</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-507-Organization-TouchPoint" see="field:BT-507-Organization-TouchPoint">cac:PostalAddress/cbc:CountrySubentityCode</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-510_a_-Organization-TouchPoint" see="field:BT-510(a)-Organization-TouchPoint">cac:PostalAddress/cbc:StreetName</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-510_b_-Organization-TouchPoint" see="field:BT-510(b)-Organization-TouchPoint">cac:PostalAddress/cbc:AdditionalStreetName</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-510_c_-Organization-TouchPoint" see="field:BT-510(c)-Organization-TouchPoint">cac:PostalAddress/cac:AddressLine/cbc:Line</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-512-Organization-TouchPoint" see="field:BT-512-Organization-TouchPoint">cac:PostalAddress/cbc:PostalZone</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-513-Organization-TouchPoint" see="field:BT-513-Organization-TouchPoint">cac:PostalAddress/cbc:CityName</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-514-Organization-TouchPoint" see="field:BT-514-Organization-TouchPoint">cac:PostalAddress/cac:Country/cbc:IdentificationCode</diagnostic>
		<diagnostic id="ND-Touchpoint_BT-739-Organization-TouchPoint" see="field:BT-739-Organization-TouchPoint">cac:Contact/cbc:Telefax</diagnostic>
		<diagnostic id="ND-UBO_BT-503-UBO" see="field:BT-503-UBO">cac:Contact/cbc:Telephone</diagnostic>
		<diagnostic id="ND-UBO_BT-506-UBO" see="field:BT-506-UBO">cac:Contact/cbc:ElectronicMail</diagnostic>
		<diagnostic id="ND-UBO_BT-507-UBO" see="field:BT-507-UBO">cac:ResidenceAddress/cbc:CountrySubentityCode</diagnostic>
		<diagnostic id="ND-UBO_BT-510_a_-UBO" see="field:BT-510(a)-UBO">cac:ResidenceAddress/cbc:StreetName</diagnostic>
		<diagnostic id="ND-UBO_BT-510_b_-UBO" see="field:BT-510(b)-UBO">cac:ResidenceAddress/cbc:AdditionalStreetName</diagnostic>
		<diagnostic id="ND-UBO_BT-510_c_-UBO" see="field:BT-510(c)-UBO">cac:ResidenceAddress/cac:AddressLine/cbc:Line</diagnostic>
		<diagnostic id="ND-UBO_BT-512-UBO" see="field:BT-512-UBO">cac:ResidenceAddress/cbc:PostalZone</diagnostic>
		<diagnostic id="ND-UBO_BT-513-UBO" see="field:BT-513-UBO">cac:ResidenceAddress/cbc:CityName</diagnostic>
		<diagnostic id="ND-UBO_BT-514-UBO" see="field:BT-514-UBO">cac:ResidenceAddress/cac:Country/cbc:IdentificationCode</diagnostic>
		<diagnostic id="ND-UBO_BT-706-UBO" see="field:BT-706-UBO">efac:Nationality/cbc:NationalityID</diagnostic>
		<diagnostic id="ND-UBO_BT-739-UBO" see="field:BT-739-UBO">cac:Contact/cbc:Telefax</diagnostic>
		<diagnostic id="ND-WinnerChosenUnpublish" see="node:ND-WinnerChosenUnpublish">efac:FieldsPrivacy</diagnostic>
	</diagnostics>
</schema>