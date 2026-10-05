# Datasource yml files are the single source of truth for datasource identity

*Date: 2026-10-02*

The datasource yml files in `common-properties` (`lib/src/main/resources/datasources/*.yml`) are the only place that defines a datasource's `type`, its identifying keys, and how a JDBC URL resolves to that `type`. Every other copy of this knowledge is legacy and is being removed: `DbReferenceFactory`'s per-dialect mapping in `connector-commons` is one, and connector-side runtime overrides are another. A datasource identifier becomes permanent catalog identity, and cross-connector references resolve only when every connector emits exactly the same identity. A second, hand-maintained mapping would drift from the yml without anyone noticing. Those cross-connector references include the generic JDBC connector and the dedicated connector that may replace it later.

## Consequences

- When the generic JDBC connector resolves a URL to an engine that has no yml yet (e.g. `jdbc:sqlite:`), the `type` it emits becomes binding: any future dedicated yml for that engine must declare that same `type`, or previously imported items are orphaned.
- New dialect knowledge goes into a yml file, never into `DbReferenceFactory` or connector code.
