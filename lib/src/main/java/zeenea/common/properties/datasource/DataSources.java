package zeenea.common.properties.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import zeenea.common.properties.datasource.reference.SourceReference;

public enum DataSources {
  CUSTOM,
  ATHENA,
  AZURE,
  BIGQUERY,
  DATABRICKS,
  DB2,
  DBT,
  DENODO,
  GENERIC_JDBC,
  GLUE,
  INFORMIX,
  KAFKA,
  LOCAL,
  MYSQL,
  MSFABRIC,
  ORACLE,
  PALANTIR_FOUNDRY,
  POSTGRESQL,
  REDSHIFT,
  S3,
  SNOWFLAKE,
  SQLSERVER;

  private final SourceReference sourceReference;

  DataSources() {
    this.sourceReference = reference(this.name().toLowerCase());
  }

  public Set<String> getMatchingKeys() {
    return new HashSet<>(this.sourceReference.getDatasource().getKeys());
  }

  public SourceReference getSourceReference() {
    return sourceReference;
  }

  private static SourceReference reference(String name) {
    ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    String fileName = "datasources/" + name + ".yml";
    InputStream input = DataSources.class.getClassLoader().getResourceAsStream(fileName);
    if (input == null) {
      throw new IllegalStateException("Missing datasource YAML file: " + fileName);
    }
    try {
      return yamlMapper.readValue(input, SourceReference.class);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to parse YAML file: " + fileName, e);
    }
  }
}
