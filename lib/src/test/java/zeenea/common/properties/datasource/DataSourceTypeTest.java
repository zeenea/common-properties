package zeenea.common.properties.datasource;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DataSourceTypeTest {

  @Test
  void typeValue_isTheYamlType() {
    assertThat(DataSourceType.POSTGRES.getTypeValue()).isEqualTo("postgres");
    assertThat(DataSourceType.SQLSERVER.getTypeValue()).isEqualTo("sqlserver");
    assertThat(DataSourceType.DBT_ETL.getTypeValue()).isEqualTo("dbt-etl");
    assertThat(DataSourceType.AWS_GLUE_DATACATALOG.getTypeValue())
        .isEqualTo("aws-glue-datacatalog");
  }

  @Test
  void matchingKeys_areUnchanged() {
    assertThat(DataSourceType.SQLSERVER.getMatchingKeys()).containsExactly("host", "port");
    assertThat(DataSourceType.SNOWFLAKE.getMatchingKeys()).containsExactly("account_id");
    assertThat(DataSourceType.BIGQUERY.getMatchingKeys()).isEmpty();
  }
}
