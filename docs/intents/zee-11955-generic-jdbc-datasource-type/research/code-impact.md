---
topic: code-impact
question: What in the current code constrains a dynamic Datasource type for generic JDBC?
requested-by: archi
date: 2026-10-05
---

# Research — Code impact

Read-only exploration of local checkouts on 2026-10-02; may lag behind remote branches.

## Already in our stack

### common-properties
- Branch `ZEE-11955/remove-generic-jdbc-type-value-rule`, commit `949b51a` (Predrag Maksimovic, 2026-10-02): removes `values: type: jdbc` from `generic-jdbc.yml` and the JDBC lines from `DataSourceType.approved.java` / `DatasetIdentificationKeys.approved.java` (ticket option 1). Uncommitted on top: `host?`, `port?`, `schema?`.
- `buildSrc/src/main/java/GenerateDataSourceTypeTask.java`:
  - `:94-98` a yml with no `datasource.values.type` is silently dropped.
  - `:102-103` matching keys = all keys except `type`; `?` is not understood (`"host?"` would be emitted literally).
  - `:107` enum name derives from the `values.type` value.
- No other datasource yml lacks `values.type`; none uses `?`. `bigquery.yml` has no host/port at all (it is not a `host?`/`port?` precedent).
- TypeScript side (`ts/`) does not read the datasources yml: not impacted.

### connector-commons
- Referential generator in `buildSrc/src/main/kotlin/zeenea/gradle/referential/` (`ReferentialGenerator.kt:124-144`): a key with a `values` entry becomes a constant validated in `validate()`; other keys become required `of()` parameters.
- `?` unsupported: `host?` produces `HOST?_KEY` (invalid Java). Template `java-datasource-identifier.mustache:39-45` requires the key list to equal `validKeys` exactly.
- Registered in `connector-commons/connector-commons/build.gradle.kts:53-56` (`jdbc` → `generic-jdbc.yml`, prefix `Jdbc`).
- Current API: `JdbcDataSourceIdentifier.of(host, port)`, `TYPE_VALUE = "jdbc"`; `JdbcTableIdentifier.of(catalog, schema, table)`.
- A separate `zeenea/referential-generator` repo is listed in the DIP Context: canonical copy unclear.

### datacatalog (backend)
- `referentiel/.../DataSourceTypeMatching.scala:10-11`: type compared to the enum name, case-insensitive, `-`→`_`, otherwise literal.
- `service/.../DataSourceIdentifierMatcher.scala:24-38`: types equal, then every matching key of the type present and equal on both sides.
- `service/.../DatasetIdentificationService.scala:21-46`: `Unsupported connector type` when the type is missing from `ACCEPTED_KEY_ORDERS`.

### Callers
- `sync-connector-plugin-template/.../JdbcConnection.java:31` and `inventory-connector-plugin-template/.../JdbcConnection.java:40,183` call `JdbcDataSourceIdentifier.of(host, port)` / `JdbcTableIdentifier.of`.
- v1 `jdbc-connector-plugin` emits no DataSourceIdentifier.
- JDBC v2 prototype (ticket option 3, runtime override): not found locally.

### Specialized ymls

| yml | `type` | datasource keys | dataset keys |
|---|---|---|---|
| sqlserver | sqlserver | host, port | catalog/schema/table |
| postgresql | **postgres** | host, port | catalog/schema/table |
| oracle | oracle | host, port | catalog/schema/table |
| mysql / mariadb / db2 / redshift | same as file | host, port | catalog/schema/table |
| snowflake | snowflake | **account_id** | catalog/schema/table |
| databricks | databricks | **host** | catalog/schema/table |
| bigquery | bigquery | **none** | **project/dataset/table** |
| generic-jdbc (branch) | none | host?, port? | catalog/**schema?**/table |

## Options

Not a technology choice; constraints the SPEC must address:

| Constraint | Consequence |
|---|---|
| Generator drops yml without `values.type` | Needs an explicit dynamic-type marker or the datasource vanishes silently |
| Neither generator supports `?` | Template and `validate()`/`of()`/getters changes in connector-commons; common-properties enum generation too |
| Backend requires every matching key | Missing host or port means no match: default port, host-less URLs need a rule |
| `schema?` vs required `schema` | Items from schema-less engines don't match specialized identifiers |
| `DataSourceType.JDBC` removed | Existing `type=jdbc` data in datacatalog: migrate or keep compatibility |
| Snowflake / Databricks / BigQuery coordinates | Not reachable through host/port |
| `of(host, port)` callers | Templates break on signature change |

## Recommendation

No recommendation: input for SPEC decisions listed in `archi.md`.

## Sources
- Paths above; DIP Context `repos.md` (referential-generator entry), `glossary.md` ("Data Source").
