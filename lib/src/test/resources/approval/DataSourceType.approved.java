package zeenea.common.properties.datasource;

import java.util.List;

public enum DataSourceType {
  AWS_GLUE_DATACATALOG("aws-glue-datacatalog", "region", "account"),
  AWS_GLUE_ETL("aws-glue-etl", "region", "account"),
  BIGQUERY("bigquery"),
  DATABRICKS("databricks", "host"),
  DB2("db2", "host", "port"),
  DBT_CLOUD("dbt-cloud", "host"),
  DBT_ETL("dbt-etl"),
  MARIADB("mariadb", "host", "port"),
  MATILLION_DPC("matillion-dpc"),
  MATILLION_ETL("matillion-etl", "host", "port"),
  MSFABRIC("msfabric"),
  MYSQL("mysql", "host", "port"),
  ORACLE("oracle", "host", "port"),
  PALANTIR_FOUNDRY("palantir-foundry", "host"),
  POSTGRES("postgres", "host", "port"),
  REDSHIFT("redshift", "host", "port"),
  SNOWFLAKE("snowflake", "account_id"),
  SQLSERVER("sqlserver", "host", "port"),
  SSIS("ssis"),
  STRATEGY("strategy", "host", "port"),
  TABLEAU("tableau", "host", "site");

  private final String typeValue;
  private final List<String> matchingKeys;

  DataSourceType(String typeValue, String... matchingKeys) {
    this.typeValue = typeValue;
    this.matchingKeys = List.of(matchingKeys);
  }

  public String getTypeValue() {
    return typeValue;
  }

  public List<String> getMatchingKeys() {
    return matchingKeys;
  }
}
