@tedefo-5304
Feature: Fields - listName attribute fields agree with the listName of their element
  TEDEFO-5304: The "-List" attribute field of a code element has a presetValue (SDK 1) or a
  calculatedValue (SDK 2) equal to the listName given by the codeList property of the element:
  the parentId (SDK 1) or listName (SDK 2) if present, else the codelist id.
  A listName attribute field that is a business field (BT-03-notice) is not checked.
  Test files under "src/test/resources/eforms-sdk-tests/tedefo-5304"

  Scenario: SDK 1 - preset values match the listName of their element
    Given The following rules
      | The preset value of a listName attribute field is the listName of its element |
    And A "tedefo-5304" folder with "valid" files
    When I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario: SDK 1 - a missing preset value and a different preset value are reported
    Given The following rules
      | The preset value of a listName attribute field is the listName of its element |
    And A "tedefo-5304" folder with "invalid" files
    When I load all fields
    And I execute validation
    Then Rule "The preset value of a listName attribute field is the listName of its element" should have been fired
    And I should get 2 SDK validation errors

  Scenario: SDK 2 - calculated values compute the listName of their element
    Given The following rules
      | The calculated value of a listName attribute field computes the listName of its element |
    And A "tedefo-5304" folder with "valid-sdk2" files
    When I load all fields
    And I execute validation
    Then I should get 0 SDK validation errors

  Scenario: SDK 2 - a missing calculated value and another listName are reported
    Given The following rules
      | The calculated value of a listName attribute field computes the listName of its element |
    And A "tedefo-5304" folder with "invalid-sdk2" files
    When I load all fields
    And I execute validation
    Then Rule "The calculated value of a listName attribute field computes the listName of its element" should have been fired
    And I should get 2 SDK validation errors
