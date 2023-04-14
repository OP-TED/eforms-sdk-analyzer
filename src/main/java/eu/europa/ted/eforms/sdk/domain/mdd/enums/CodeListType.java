package eu.europa.ted.eforms.sdk.domain.mdd.enums;

public enum CodeListType {
  flat("flat"),

  hierarchical("hierarchical");

  private final String literal;

  private CodeListType(String literal) {
    this.literal = literal;
  }

//  public Catalog getCatalog() {
//    return null;
//  }
//
//  public Schema getSchema() {
//    return null;
//  }
//
//  public String getName() {
//    return "code_list_type";
//  }
//
//  public String getLiteral() {
//    return literal;
//  }
}
