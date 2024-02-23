package eu.europa.ted.eforms.sdk.analysis.domain.codelist;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CodelistsIndexItem implements Comparable<CodelistsIndexItem> {
  private String id;
  private String version;
  private String parentId;
  private String filename;
  private String description;

  @JsonProperty("_label")
  private String labelId;

  public String getId() {
    return id;
  }

  public String getParentId() {
    return parentId;
  }

  public String getVersion() {
    return version;
  }

  public String getFilename() {
    return filename;
  }

  public String getDescription() {
    return description;
  }

  public String getLabelId() {
    return labelId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CodelistsIndexItem other = (CodelistsIndexItem) obj;
    return Objects.equals(id, other.id);
  }

  @Override
  public int compareTo(final CodelistsIndexItem o) {
    // We could also imagine involving the parentId, but I think sorting by the unique id is better.
    return this.getId().compareTo(o.getId());
  }
}
