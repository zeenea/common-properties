# Common Properties

The shared vocabulary connectors and the Zeenea platform use to describe and identify data sources.

## Language

**Datasource type**:
The stable name of a data source engine (e.g. `sqlserver`, `postgres`, `sqlite`), carried as the `type` key of a **Datasource identifier**.
_Avoid_: subprotocol, dialect, connector id

**Datasource identifier**:
The set of keys (`type` plus engine-specific coordinates such as `host`, `port`) that identifies one **Data Source** across every connector.
_Avoid_: connection id

**Specialized datasource**:
A data source engine whose identity is declared statically by its own yml file (fixed **Datasource type**).

**Generic JDBC datasource**:
A data source reached through the generic JDBC connector, whose **Datasource type** is resolved from the JDBC URL instead of being fixed.
_Avoid_: "jdbc datasource" (JDBC is a protocol, not a data source)

## Relationships

- A **Generic JDBC datasource** resolves to exactly one **Datasource type**, derived from the JDBC subprotocol.
- A future yml for an engine first seen through generic JDBC must reuse the **Datasource type** generic JDBC already emitted.

## Example dialogue

> **Dev:** "A customer scanned SQLite with the generic JDBC connector — what **Datasource type** did it get?"
> **Domain expert:** "`sqlite`. And if we ever ship a dedicated SQLite connector, its yml must say `type: sqlite` too, so both resolve to the same **Data Source**."

## Flagged ambiguities

- "jdbc" was used as a **Datasource type** (`type: jdbc` in `generic-jdbc.yml`) — resolved: JDBC is a protocol; the type is always the resolved engine.
- **Open — potential problem:** the JDBC subprotocol does not always equal an existing **Specialized datasource**'s **Datasource type** (`jdbc:postgresql` vs `type: postgres`; `jdbc:jtds:sqlserver` vs `type: sqlserver`). Unresolved: no aliasing rule is decided.
