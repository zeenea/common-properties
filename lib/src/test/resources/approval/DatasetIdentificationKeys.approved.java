package zeenea.common.properties.datasource;

import java.util.List;
import java.util.Map;

public final class DatasetIdentificationKeys {

  public static final Map<DataSourceType, List<List<String>>> ACCEPTED_KEY_ORDERS =
      Map.ofEntries(
          Map.entry(DataSourceType.BIGQUERY, List.of(List.of("project", "dataset", "table"))),
          Map.entry(DataSourceType.DATABRICKS, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.DB2, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.JDBC, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.MARIADB, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(
              DataSourceType.MSFABRIC,
              List.of(
                  List.of("workspace_id", "type", "id", "dataset_name"),
                  List.of("workspace_id", "type", "id", "table_name"))),
          Map.entry(DataSourceType.MYSQL, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.ORACLE, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.PALANTIR_FOUNDRY, List.of(List.of("rid"))),
          Map.entry(DataSourceType.POSTGRES, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.REDSHIFT, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.SNOWFLAKE, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.SQLSERVER, List.of(List.of("catalog", "schema", "table"))),
          Map.entry(DataSourceType.TABLEAU, List.of(List.of("id", "datasource_type"))));

  private DatasetIdentificationKeys() {}
}
