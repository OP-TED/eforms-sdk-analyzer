package eu.europa.ted.eforms.sdk.analysis.fact;

import java.io.Serializable;
import java.lang.reflect.Type;
import eu.europa.ted.eforms.sdk.analysis.Identifiable;

public interface SdkComponentFact<ID extends Serializable> extends Identifiable<ID>, Type {
}
