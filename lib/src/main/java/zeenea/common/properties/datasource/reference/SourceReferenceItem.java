package zeenea.common.properties.datasource.reference;

import java.util.List;

public class SourceReferenceItem {

  private String name;

  private SourceReferenceIdentification label;

  private SourceReferenceIdentification identification;

  private List<String> fields;

  public SourceReferenceItem() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SourceReferenceIdentification getLabel() {
    return label;
  }

  public void setLabel(SourceReferenceIdentification label) {
    this.label = label;
  }

  public SourceReferenceIdentification getIdentification() {
    return identification;
  }

  public void setIdentification(SourceReferenceIdentification identification) {
    this.identification = identification;
  }

  public List<String> getFields() {
    return fields;
  }

  public void setFields(List<String> fields) {
    this.fields = fields;
  }
}
