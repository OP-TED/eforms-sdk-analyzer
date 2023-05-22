package eu.europa.ted.eforms.sdk.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Represents an object identified with a literal string.
 *
 */
public interface ILiteral {
  @JsonValue
  String getLiteral();
}
