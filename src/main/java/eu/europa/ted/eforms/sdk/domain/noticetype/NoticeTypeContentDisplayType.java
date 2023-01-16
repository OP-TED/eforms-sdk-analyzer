package eu.europa.ted.eforms.sdk.domain.noticetype;

import org.jooq.EnumType;

/**
 * Defines how to display a content in the user interface.
 */
public enum NoticeTypeContentDisplayType implements EnumType {
  /**
   * The GROUP display type is used to help the editor. It is not really required as content having
   * one or more children is a group by definition (a container grouping fields).
   */
  GROUP("GROUP"),

  /**
   * This is equivalent to the old "section: true" boolean.
   */
  SECTION("SECTION"),

  /**
   * For example an HTML textarea.
   */
  TEXTAREA("TEXTAREA"),

  /**
   * For example an HTML input.
   */
  TEXTBOX("TEXTBOX"),

  /**
   * For example an HTML input of type checkbox.
   */
  RADIO("RADIO"),

  /**
   * For example an HTML select.
   */
  COMBOBOX("COMBOBOX"),

  /**
   * For example an HTML input of type checkbox.
   */
  CHECKBOX("CHECKBOX");

  private final String literal;

  NoticeTypeContentDisplayType(final String literal) {
    this.literal = literal;
  }

  public String getLiteral() {
    return literal;
  }

  @Override
  public String getName() {
    return "notice_type_content_display_type";
  }
}
