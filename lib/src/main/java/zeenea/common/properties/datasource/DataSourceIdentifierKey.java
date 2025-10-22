package zeenea.common.properties.datasource;

import java.util.Objects;

public final class DataSourceIdentifierKey {

  private final String key;

  public DataSourceIdentifierKey(String key) {
    this.key = key;
  }

  public String key() {
    return key;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (DataSourceIdentifierKey) obj;
    return Objects.equals(this.key, that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }

  @Override
  public String toString() {
    return "DataSourceIdentifierKey[key=" + key + "]";
  }
}
