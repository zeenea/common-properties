package zeenea.common.properties.datasource.reference;

import java.util.List;
import java.util.Map;

public class SourceReferenceIdentification {

  private List<String> keys;
  private Map<String, String> values;

  public SourceReferenceIdentification() {}

  public List<String> getKeys() {
    return keys;
  }

  public void setKeys(List<String> keys) {
    this.keys = keys;
  }

  public Map<String, String> getValues() {
    return values;
  }

  public void setValues(Map<String, String> values) {
    this.values = values;
  }
}
