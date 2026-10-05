---
slug: zee-11955-generic-jdbc-datasource-type
ticket: ZEE-11955
source: https://actian.atlassian.net/browse/ZEE-11955
branch: ZEE-11955/remove-generic-jdbc-type-value-rule
initiator: Lionel Vigier
created: 2026-10-05
stage: triaged
---

# Generic JDBC emits the specialized datasource identifier

## Problem and value
The generic JDBC v2 connector reports every datasource as `type: jdbc` (fixed in `generic-jdbc.yml`). A SQL Server reached through generic JDBC therefore never matches `sqlserver/host/port` emitted by the specialized connector or referenced by other connectors (lineage, BI). Generic JDBC must emit the same datasource identifier as the specialized connector for the same engine, and keep doing so when a dedicated connector is added later.

## Target users
- Customers cataloguing JDBC-reachable engines that have no dedicated connector (e.g. SQLite): their items must keep resolving cross-references, today and after a dedicated connector ships.

## Usage scenarios
1. A customer scans SQL Server with generic JDBC; a dbt or BI connector referencing `sqlserver/host/port` resolves to those items.
2. A customer scans SQLite with generic JDBC (type `sqlite`); later a dedicated SQLite connector ships with `type: sqlite`; existing references still resolve.

## Evidence
- No direct customer evidence; the need comes from the connector design (ZEE-11955 description and comments).

## Success criteria
- For every engine with a yml, generic JDBC and the specialized connector emit identical datasource identifiers for the same server.

## Out of scope
- Per-engine subname grammars beyond host/port (proposed, see research).

## Open questions
- See `archi.md` (`ARCHI-Q*`).

## Impacted projects
- `common-properties` — `generic-jdbc.yml`, `GenerateDataSourceTypeTask`, approved test files; single source of truth (ADR 0001).
- `connector-commons` — referential generator (`?` keys, dynamic type), `DbReferenceFactory` (legacy, to remove).
- `datacatalog` — `DataSourceIdentifierMatcher` literal matching, `DataSourceType.JDBC` removal, existing `type=jdbc` data.
- `sync-` / `inventory-connector-plugin-template` — call `JdbcDataSourceIdentifier.of(host, port)`.
- JDBC v2 connector — location TBD.

## Roster
| Role | Need | Why | Person |
|---|---|---|---|
| PM | not needed | internal identity rule, no new user capability | — |
| UX | not needed | no user-visible surface | — |
| Architect | required | cross-repo contract (DataSourceType, backend matcher), data compatibility, generator ownership | Lionel Vigier |
| Developer | required | consolidates; common-properties generation + connector-commons buildSrc | Lionel Vigier |

## Required inputs
- [ ] Architect — Confluence "2025-10 Datasource identifier — tech design" (pageId 1837498369)
- [ ] Architect — which generator is canonical (`connector-commons/buildSrc` vs `zeenea/referential-generator`)
- [ ] Developer — location of the JDBC v2 prototype (Predrag Maksimovic)
- [x] Research — `research/jdbc-url-segments.md`, `research/code-impact.md`

## How to contribute
Run `/grill zee-11955-generic-jdbc-datasource-type as archi` on branch `ZEE-11955/remove-generic-jdbc-type-value-rule`. When done, run `/grill zee-11955-generic-jdbc-datasource-type consolidate`.
