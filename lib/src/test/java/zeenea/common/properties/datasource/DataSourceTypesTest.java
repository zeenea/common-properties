package zeenea.common.properties.datasource;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DataSourceTypesTest {

  static Stream<Arguments> expectedKeys() {
    return Stream.of(
        Arguments.of("bigquery", List.of()),
        Arguments.of("db2", List.of("host", "port")),
        Arguments.of("dbt-cloud", List.of("host")),
        Arguments.of("dbt-etl", List.of()),
        Arguments.of("jdbc", List.of("host", "port")),
        Arguments.of("matillion-dpc", List.of()),
        Arguments.of("matillion-etl", List.of("host", "port")),
        Arguments.of("msfabric", List.of()),
        Arguments.of("mysql", List.of("host", "port")),
        Arguments.of("oracle", List.of("host", "port")),
        Arguments.of("postgres", List.of("host", "port")),
        Arguments.of("redshift", List.of("host", "port")),
        Arguments.of("snowflake", List.of("account_id")),
        Arguments.of("sqlserver", List.of("host", "port")));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("expectedKeys")
  void shouldParseMatchingKeys(String typeName, List<String> expectedKeys) {
    List<String> keys = DataSourceTypes.getMatchingKeys(typeName);

    assertThat(keys).hasSize(expectedKeys.size()).containsAll(expectedKeys);
  }
}
