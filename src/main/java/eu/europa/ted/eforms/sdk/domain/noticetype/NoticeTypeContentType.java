package eu.europa.ted.eforms.sdk.domain.noticetype;

import org.jooq.EnumType;

/**
 * Defines the type of a content, this is about the content data itself and not about how to display
 * it.
 */
public enum NoticeTypeContentType implements EnumType {
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

  public String getLiteral() {
    return literal;
  }

  @Override
  public String getName() {
    return "notice_type_content_type";
  }
}
