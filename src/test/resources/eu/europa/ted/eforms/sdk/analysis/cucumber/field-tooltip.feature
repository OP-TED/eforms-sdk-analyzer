@field-tooltip
Feature: Notice Types - field description (tooltip) labels
  TEDEMD-90: A field shown in a notice type definition should have a description (tooltip) label,
  on the field itself or — failing that — on its business term.
  Test files under "src/test/resources/eforms-sdk-tests/field-tooltip"

  Background:
    Given The following rules
      | Fields used in notice types have a description label |

  Scenario: Fields used in notice types have a description label
    Given A "field-tooltip" folder with "valid" files
    When I load all fields
    And I load all notice types
    And I load all labels
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario: A field used in a notice type has no description label
    Given A "field-tooltip" folder with "invalid" files
    When I load all fields
    And I load all notice types
    And I load all labels
    And I execute validation
    Then Rule "Fields used in notice types have a description label" should have been fired
    Then I should get 1 SDK validation errors
