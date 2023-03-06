package eu.europa.ted.eforms.sdk.util;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.commons.lang3.Validate;
import org.jooq.EnumType;

public class EnumHelper {
  private EnumHelper() {}

  public static String getLiteral(EnumType enumType) {
    return enumType != null ? enumType.getLiteral() : null;
  }

  public static <T extends EnumType> T getEnum(final Class<T> enumType,
      final String literal) {
    Validate.notNull(enumType, "Undefined enum type");

    Optional<T> result = Stream.of(enumType.getEnumConstants())
        .filter((EnumType c) -> c.getLiteral().equals(literal))
        .findFirst();

    if (!result.isPresent()) {
      throw new IllegalArgumentException(
          MessageFormat.format("Could not find enum of type [{0}] with literal [{1}]",
              enumType.getClass().getName(), literal));
    }

    return result.get();
  }
}
