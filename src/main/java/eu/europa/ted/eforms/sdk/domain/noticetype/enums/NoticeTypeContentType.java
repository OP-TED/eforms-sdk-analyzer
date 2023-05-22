package eu.europa.ted.eforms.sdk.domain.noticetype.enums;

import eu.europa.ted.eforms.sdk.domain.ILiteral;

/**
 * Defines the type of a content, this is about the content data itself and not about how to display
 * it.
 */
public enum NoticeTypeContentType implements ILiteral {
  /**
   * This is a form field in the UI.
   */
  FIELD("field"),

  /**
   * This is a group of form field, but we do not have an associated node id!
   */
  GROUP("group");

  private final String literal;

  NoticeTypeContentType(String literal) {
    this.literal = literal;
  }

  @Override
  public String getLiteral() {
    return literal;
  }
}
