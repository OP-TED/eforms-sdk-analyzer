@tedefo-1806
Feature: Fields - Field validation
  TEDEFO-1806: Fields of type code have a codelist property
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-1806"

  Background:
    Given The following rules
      | All fields of type code have a codelist property |

  Scenario: All fields of type code have a codelist property
    Given A "tedefo-1806" folder with "valid" files
    When I load all fields
    And I execute validation
    Then I should get 0 SDK validation warnings

  Scenario Outline: Some fields of type code do not have a codelist property
    Given A "tedefo-1806" folder with "invalid" files
    When I load all fields
    And I execute validation
    Then Rule "<expected rule>" should have been fired
    And I should get 2 SDK validation warnings

    Examples:
      | expected rule                     |
      | All fields of type code have a codelist property |
