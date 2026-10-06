@tedefo-5254
Feature: Fields - Field validation
  TEDEFO-5254: Multiple co-constraint rules for the same field and notice subtype are allowed
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-5254"
  Background:
    Given The following rules
      | Field constraints do not have duplicate notice types identifiers |
  Scenario: A field has several co-constraint rules for the same notice subtype
    Given A "tedefo-5254" folder with "valid" files
    When I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors
  Scenario Outline: A single-valued property still reports duplicate notice subtypes
    Given A "tedefo-5254" folder with "invalid" files
    When I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 1 SDK validation errors
    Examples:
      | expected rule                                                    |
      | Field constraints do not have duplicate notice types identifiers |
