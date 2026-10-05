---
role: archi
authors: [Lionel Vigier]
status: in-progress
updated: 2026-10-05
---

# Architect — Generic JDBC emits the specialized datasource identifier

## Inputs
- Jira ZEE-11955 description and comments (option 3 is the current prototype).
- `research/code-impact.md`, `research/jdbc-url-segments.md`.
- Missing: Confluence "2025-10 Datasource identifier — tech design" (pageId 1837498369), not yet read.

## Decisions
- **ARCHI-D1** JDBC is a protocol, not a datasource: the Datasource type of a generic JDBC connection is the engine resolved from the JDBC URL, never `jdbc`. _Why:_ generic JDBC must emit the specialized connector's identifier. _Confidence:_ high
- **ARCHI-D2** Identity is forward compatible: the type and keys generic JDBC emits for an engine without a yml are binding on any future dedicated yml for that engine. _Why:_ a later dedicated connector (e.g. SQLite) must keep resolving cross-references to items already imported. _Confidence:_ high
- **ARCHI-D3** Datasource yml files in common-properties are the single source of truth for type, keys and URL → type resolution; `DbReferenceFactory` is legacy and being removed. _Why:_ a second mapping drifts silently. See `docs/adr/0001-datasource-yml-single-source-of-truth.md`. _Confidence:_ high

## Questions for Developer
- **ARCHI-Q1** Vendor token ≠ Datasource type (`postgresql`/`postgres`, `hive2`, `spark`, `sap`, `informix-sqli`, `mysql+srv`, `ucanaccess`, `odbc`): flagged as a potential problem only; aliasing rule undecided. _Blocking:_ yes
- **ARCHI-Q2** Adopt the named-field model (`research/jdbc-url-segments.md`) and its identity rule, scoped to prefix fields plus `hosts` for ZEE-11955? _Recommended:_ yes; fallback is option C (first segment after skipping declared wrappers). _Blocking:_ yes
- **ARCHI-Q3** URLs with no network coordinates (`jdbc:sqlite:/path`): (a) type only, (b) `path?` coordinate now, (c) no Datasource identifier until a file-based design exists. _Recommended:_ (c); in-memory (`storage = mem`) never gets an identity. _Blocking:_ no
- **ARCHI-Q4** Multi-host URLs (`loadbalance`, `failover`): which host identifies the Data Source? _Blocking:_ no
- **ARCHI-Q5** Missing port: fill the engine default port declared in its yml? _Recommended:_ yes, the backend requires every matching key. _Blocking:_ no
- **ARCHI-Q6** `schema?` vs required `schema` in specialized ymls. _Blocking:_ no
- **ARCHI-Q7** Existing `type=jdbc` data in datacatalog: migrate or keep compatibility? _Blocking:_ yes
- **ARCHI-Q8** Explicit dynamic-type marker in yml so `GenerateDataSourceTypeTask` doesn't silently drop it. _Recommended:_ yes. _Blocking:_ no
- **ARCHI-Q9** Canonical generator: `connector-commons/buildSrc` or `zeenea/referential-generator`? _Blocking:_ yes
- **ARCHI-Q10** Location of the JDBC v2 prototype (ask Predrag Maksimovic). _Blocking:_ no

## Proposed glossary terms
Applied directly in `common-properties/CONTEXT.md` during the live session: **Datasource type**, **Datasource identifier**, **Specialized datasource**, **Generic JDBC datasource**.
