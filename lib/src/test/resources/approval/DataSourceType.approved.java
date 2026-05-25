package zeenea.common.properties.datasource;

import java.util.List;

public enum DataSourceType {
  BIGQUERY,
  DATABRICKS("host"),
  DB2("host", "port"),
  DBT_CLOUD("host"),
  DBT_ETL,
  JDBC("host", "port"),
  MARIADB("host", "port"),
  MATILLION_DPC,
  MATILLION_ETL("host", "port"),
  MSFABRIC,
  MYSQL("host", "port"),
  ORACLE("host", "port"),
  POSTGRES("host", "port"),
  REDSHIFT("host", "port"),
  SNOWFLAKE("account_id"),
  SQLSERVER("host", "port"),
  TABLEAU("host", "site");

  private final List<String> matchingKeys;

  DataSourceType(String... matchingKeys) {
    this.matchingKeys = List.of(matchingKeys);
  }

  public List<String> getMatchingKeys() {
    return matchingKeys;
  }
}
