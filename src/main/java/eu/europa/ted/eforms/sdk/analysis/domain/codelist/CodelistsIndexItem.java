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
    return this.id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public String getParentId() {
    return this.parentId;
  }

  public void setParentId(final String parentId) {
    this.parentId = parentId;
  }

  public String getVersion() {
    return this.version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  public String getFilename() {
    return this.filename;
  }

  public void setFilename(final String filename) {
    this.filename = filename;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public String getLabelId() {
    return this.labelId;
  }

  public void setLabelId(final String labelId) {
    this.labelId = labelId;
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
