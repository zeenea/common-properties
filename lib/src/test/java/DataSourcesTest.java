import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import zeenea.common.properties.datasource.DataSources;
import zeenea.common.properties.datasource.reference.SourceReference;

class DataSourcesTest {

  @Test
  void enumSizeDoesNotChange() {
    // Verify all expected enum names exist
    Set<String> expectedNames =
        Set.of(
            "ATHENA",
            "AZURE",
            "BIGQUERY",
            "CUSTOM",
            "DATABRICKS",
            "DB2",
            "DBT",
            "DENODO",
            "GENERIC_JDBC",
            "GLUE",
            "INFORMIX",
            "KAFKA",
            "LOCAL",
            "MSFABRIC",
            "MYSQL",
            "ORACLE",
            "PALANTIR_FOUNDRY",
            "POSTGRESQL",
            "REDSHIFT",
            "S3",
            "SNOWFLAKE",
            "SQLSERVER");

    Set<String> actualNames =
        Set.of(
            java.util.Arrays.stream(DataSources.values()).map(Enum::name).toArray(String[]::new));

    assertThat(actualNames)
        .as("DataSources enum names should not be renamed or removed")
        .containsExactlyInAnyOrder(expectedNames.toArray(new String[0]));

    // Verify each enum value has a corresponding test method
    Set<String> testMethodNames =
        Arrays.stream(DataSourcesTest.class.getDeclaredMethods())
            .filter(m -> m.isAnnotationPresent(Test.class))
            .map(Method::getName)
            .collect(Collectors.toSet());

    for (DataSources dataSource : DataSources.values()) {
      String expectedMethodName = dataSource.name().toLowerCase();
      assertThat(testMethodNames)
          .as(
              "Test method '%s' should exist for enum value %s",
              expectedMethodName, dataSource.name())
          .contains(expectedMethodName);
    }
  }

  @Test
  void athena() {
    SourceReference ref = DataSources.ATHENA.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("athena");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("aws-athena"));
    assertThat(ref.getDatasource().getKeys())
        .isEqualTo(List.of("type", "schema", "stagingDir", "catalog", "regionName"));
    assertNull(ref.getDatasets());
  }

  @Test
  void azure() {
    SourceReference ref = DataSources.AZURE.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("azure");
    assertThat(ref.getConnectorIds())
        .isEqualTo(List.of("azure-datafactory", "azure-synapse-data", "azure-purview"));
    assertThat(ref.getDatasource().getKeys())
        .isEqualTo(List.of("type", "location", "format", "delimiter"));
  }

  @Test
  void bigquery() {
    SourceReference ref = DataSources.BIGQUERY.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("bigquery");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("BigQuery", "BigQueryOrganization"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type"));
    assertNotNull(ref.getDatasets());
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Dataset");
    assertThat(ref.getDatasets().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("project", "dataset", "table"));
  }

  @Test
  void custom() {
    SourceReference ref = DataSources.CUSTOM.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isNull();
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("TODO"));
    assertTrue(ref.getDatasource().getKeys().isEmpty());
  }

  @Test
  void databricks() {
    SourceReference ref = DataSources.DATABRICKS.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("databricks");
    assertThat(ref.getConnectorIds())
        .isEqualTo(
            List.of(
                // "databricks-hivemetastore",
                "databricks-unitycatalog", "databricks-jdbc"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host"));
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Catalog Item");
    assertThat(ref.getDatasets().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("catalog", "schema", "table"));
    assertThat(ref.getDatasets().get(0).getFields()).isEqualTo(List.of("field_id"));
    assertThat(ref.getDataProcesses().size()).isEqualTo(1);
    assertThat(ref.getDataProcesses().get(0).getName()).isEqualTo("Job");
    assertThat(ref.getDataProcesses().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("job_id"));
  }

  @Test
  void db2() {
    SourceReference ref = DataSources.DB2.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("db2");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("DB2"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host", "port"));
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Table");
    assertThat(ref.getDatasets().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("database", "schema", "table"));
  }

  @Test
  void dbt() {
    SourceReference ref = DataSources.DBT.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("dbt");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("dbt-cloud", "dbt"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type"));
    assertThat(ref.getDatasets().size()).isEqualTo(0);
  }

  @Test
  void denodo() {
    SourceReference ref = DataSources.DENODO.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("denodo");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("denodo"));
    assertThat(ref.getDatasource().getKeys())
        .isEqualTo(List.of("type", "host", "port", "database"));
    assertNull(ref.getDatasets());
  }

  @Test
  void generic_jdbc() {
    SourceReference ref = DataSources.GENERIC_JDBC.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("jdbc");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("generic-jdbc"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host", "port"));
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Table");
    assertThat(ref.getDatasets().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("catalog", "schema", "table"));
  }

  @Test
  void glue() {
    SourceReference ref = DataSources.GLUE.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("glue");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("aws.glue", "aws-glue-etl"));
    assertThat(ref.getDatasource().getKeys())
        .isEqualTo(List.of("type", "account", "database", "location", "format"));
  }

  @Test
  void informix() {
    SourceReference ref = DataSources.INFORMIX.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("informix");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("actian-informix"));
    assertThat(ref.getDatasource().getKeys())
        .isEqualTo(List.of("type", "host", "port", "database"));
  }

  @Test
  void kafka() {
    SourceReference ref = DataSources.KAFKA.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("kafka");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("kafka"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host", "format"));
  }

  @Test
  void local() {
    SourceReference ref = DataSources.LOCAL.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("local");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("local-filesystem"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "path", "format"));
  }

  @Test
  void msfabric() {
    SourceReference ref = DataSources.MSFABRIC.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("msfabric");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("msfabric", "powerbi-v2"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type"));
    assertNotNull(ref.getDataProcesses());
    assertTrue(ref.getDataProcesses().isEmpty());
    assertThat(ref.getVisualizations().size()).isEqualTo(1);
    assertThat(ref.getVisualizations().get(0).getName()).isEqualTo("Report");
    assertThat(ref.getVisualizations().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("workspace_id", "type", "id"));
    assertThat(ref.getDatasets().size()).isEqualTo(3);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Semantic Model");
    assertThat(ref.getDatasets().get(1).getName()).isEqualTo("Warehouse");
    assertThat(ref.getDatasets().get(2).getName()).isEqualTo("Lakehouse");
    assertThat(ref.getDatasets().get(0).getFields()).isEqualTo(List.of("field_key"));
  }

  @Test
  void mysql() {
    SourceReference ref = DataSources.MYSQL.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("mysql");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("Mysql"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host", "port"));
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Table");
    assertThat(ref.getDatasets().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("database", "schema", "table"));
  }

  @Test
  void oracle() {
    SourceReference ref = DataSources.ORACLE.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("oracle");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("Oracle"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("host", "port", "serviceName"));
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Table");
  }

  @Test
  void palantir_foundry() {
    SourceReference ref = DataSources.PALANTIR_FOUNDRY.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("palantir-foundry");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("palantir"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host", "port"));
  }

  @Test
  void postgresql() {
    SourceReference ref = DataSources.POSTGRESQL.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("postgres");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("PgSql"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host", "port"));
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Table");
    assertThat(ref.getDatasets().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("database", "schema", "table"));
  }

  @Test
  void redshift() {
    SourceReference ref = DataSources.REDSHIFT.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("redshift");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("AWSRedshift"));
    assertThat(ref.getDatasource().getKeys())
        .isEqualTo(List.of("database", "schema", "host", "region", "account"));
  }

  @Test
  void s3() {
    SourceReference ref = DataSources.S3.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("s3");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("AmazonS3"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("bucket"));
  }

  @Test
  void snowflake() {
    SourceReference ref = DataSources.SNOWFLAKE.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("snowflake");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("snowflake-v2"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host", "port", "account"));
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Table");
    assertThat(ref.getDatasets().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("database", "schema", "table"));
  }

  @Test
  void sqlserver() {
    SourceReference ref = DataSources.SQLSERVER.getSourceReference();
    assertThat(ref.getDatasource().getValues().get("type")).isEqualTo("sqlserver");
    assertThat(ref.getConnectorIds()).isEqualTo(List.of("SQLServer"));
    assertThat(ref.getDatasource().getKeys()).isEqualTo(List.of("type", "host", "port"));
    assertThat(ref.getDatasets().size()).isEqualTo(1);
    assertThat(ref.getDatasets().get(0).getName()).isEqualTo("Table");
    assertThat(ref.getDatasets().get(0).getIdentification().getKeys())
        .isEqualTo(List.of("database", "schema", "table"));
  }
}
