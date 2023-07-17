package eu.europa.ted.eforms.sdk.analysis;

import java.io.Serializable;

public interface Identifiable<ID extends Serializable> extends Serializable {
  ID getId();
}
