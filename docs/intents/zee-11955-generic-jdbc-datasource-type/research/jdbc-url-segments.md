---
topic: jdbc-url-segments
question: How should generic JDBC derive the Datasource type (and identifying coordinates) from a JDBC URL?
requested-by: archi
date: 2026-10-05
---

# Research — JDBC URL segments

URL shapes below come from the drivers' documented formats; they have **not** been checked against customer configurations.

## Already in our stack

- `connector-commons/connector-commons/src/main/java/zeenea/connector/commons/db/DbReferenceFactory.java`
  - `JDBC_SUBPROTOCOL_TO_ENGINE` (`:113-127`): first segment, plus a hard-coded `jtds:sqlserver` entry.
  - `parseJdbcUrl` (`:130-179`): authority URLs (`://`) yield host/port/database (default port per engine); non-authority URLs yield the engine only.
  - Legacy, to be removed (ADR 0001): kept here as prior art only.
- v1 `jdbc-connector-plugin/.../generic/GenericJdbcConnectorFactory.scala:45-47` parses the subprotocol for identifier quoting.

## Positional rules tested

`first` = first segment after `jdbc:`; `last` = last segment before the subname, splitting on `:`.

### Authority URLs (`jdbc:<…>://host…`)

| URL | first | last | existing yml `type` |
|---|---|---|---|
| `jdbc:sqlserver://h:1433;…` | ✅ sqlserver | ✅ | sqlserver |
| `jdbc:postgresql://h/db` | ⚠️ postgresql | ⚠️ | **postgres** |
| `jdbc:jtds:sqlserver://h/db` | ❌ jtds | ✅ sqlserver | sqlserver |
| `jdbc:jtds:sybase://h/db` | ❌ jtds (merged with SQL Server) | ✅ sybase | — |
| `jdbc:aws-wrapper:postgresql://…` | ❌ | ✅ (⚠️ postgres) | postgres |
| `jdbc:p6spy:mysql://…`, `log4jdbc:`, `otel:` | ❌ | ✅ | varies |
| `jdbc:mysql:loadbalance://h1,h2/db` (`:replication`) | ✅ | ❌ | mysql |
| `jdbc:mariadb:failover://…` (`:sequential`, `:loadbalance`) | ✅ | ❌ | mariadb |
| `jdbc:redshift:iam://cluster:region/db` | ✅ | ❌ | redshift |
| `jdbc:h2:tcp://h/~/db` | ✅ | ❌ | — |
| `jdbc:hive2://h:10000/db` | ⚠️ hive2 | ⚠️ | hive (draft) |
| `jdbc:spark://…` (legacy Databricks) | ⚠️ | ⚠️ | databricks |
| `jdbc:sap://h:30015` (HANA) | ⚠️ | ⚠️ | — |
| `jdbc:informix-sqli://h:p/db:INFORMIXSERVER=x` | ⚠️ | ⚠️ | informix (draft) |
| `jdbc:mysql+srv://…` | ⚠️ | ⚠️ | mysql |
| `jdbc:ucanaccess://C:/x.accdb` | ⚠️ | ⚠️ | — (`C` parses as host) |

### Non-authority URLs (subname contains colons)

| URL | naive split | first | last |
|---|---|---|---|
| `jdbc:oracle:thin:@h:1521/svc` (`@//h…`, `@(DESCRIPTION=…)`, `@tnsalias`, `oracle:oci:`) | oracle, thin, @h, 1521/svc | ✅ | ❌ |
| `jdbc:sqlite:/data/x.db` | sqlite, /data/x.db | ✅ | ❌ |
| `jdbc:sqlite:C:\data\x.db` | sqlite, C, \data\x.db | ✅ | ❌ |
| `jdbc:sqlite::memory:` | sqlite, "", memory, "" | ✅ | ❌ |
| `jdbc:sqlite:file:/x?mode=ro` | sqlite, file, /x?mode=ro | ✅ | ❌ |
| `jdbc:h2:mem:x`, `jdbc:h2:file:/x`, `jdbc:h2:~/x` | h2, mem/file/~ | ✅ | ❌ |
| `jdbc:hsqldb:mem:x`, `jdbc:derby:memory:x`, `jdbc:duckdb:/x` | engine, mode… | ✅ | ❌ |
| `jdbc:sybase:Tds:h:5000` | sybase, Tds, h, 5000 | ✅ | ❌ |
| `jdbc:db2:SAMPLE` | db2, SAMPLE | ✅ | ❌ |
| `jdbc:postgresql:mydb` | postgresql, mydb | ✅ (⚠️ postgres) | ❌ |
| `jdbc:odbc:MyDsn` | odbc, MyDsn | ⚠️ no engine | ❌ |

✅ correct type · ❌ wrong token · ⚠️ vendor token is not our type name, whatever the rule.

**Why positions fail:** wrappers and drivers precede the engine; variant, topology, auth and storage follow it; in non-authority URLs the subname contains colons, so splitting on `:` cannot find where the subprotocol ends.

## Named-field model

Every segment observed fits one named field; each driver fills only a few. Fields are identified by **token lookup**, not position; the first unknown token starts the subname.

| Part | Field | Examples |
|---|---|---|
| prefix | `wrapper` (0..n) | `p6spy`, `log4jdbc`, `otel`, `aws-wrapper` |
| prefix | `driver` | `jtds`, `ucanaccess` |
| prefix | **`engine`** | `sqlserver`, `postgresql`, `oracle`, `sqlite`, `h2`, `sybase` |
| prefix | `variant` | `thin`, `oci` |
| prefix | `protocol` | `tcp`, `ssl` (H2), `hsql`/`hsqls`/`http` (HSQLDB), `Tds` (Sybase) |
| prefix | `topology` | `loadbalance`, `replication`, `failover`, `sequential`, `+srv` |
| prefix | `auth` | `iam` (Redshift) |
| prefix | `storage` | `mem`, `memory`, `file` |
| subname | **`hosts`** (1..n) | `//h:1433`, `//h1,h2/`, `@h:1521`, `@(DESCRIPTION=…)` |
| subname | **`path`** | `/data/x.db`, `C:\x.db`, `~/x` |
| subname | `database` | `/db`, `;databaseName=x`, `SAMPLE` |
| subname | `service` | Oracle `/svc` or `:SID`, TNS alias, `INFORMIXSERVER` |
| subname | `properties` | `;k=v`, `?k=v`, `,DBS_PORT=1025` (Teradata port) |

Candidate identity rule: only `engine` (normalized into the Datasource type) and coordinates (`hosts` / `path`) enter the Datasource identifier; `wrapper`, `driver`, `variant`, `protocol`, `topology`, `auth` never do — `jdbc:p6spy:jtds:sqlserver://h` and `jdbc:sqlserver://h` are the same Data Source.

Limits:
- Still needs a token vocabulary; per ADR 0001 it would live in yml (shared tokens in `generic-jdbc.yml`, engine tokens and modes in each engine's yml).
- Fused or misnamed tokens (`mysql+srv`, `informix-sqli`, `hive2`, `sap`, `spark`) still need normalization; `odbc` carries no engine.
- Subname grammar is per engine (where database and port live).
- A database alias equal to a known token (e.g. `jdbc:db2:file`) would be misread.

## Options

| Option | Fit with constraints | Maturity | Ops cost | Integration effort | Pitfalls |
|---|---|---|---|---|---|
| A. First segment | Fails on wrappers/drivers; merges SQL Server and Sybase under `jtds` | n/a | none | trivial | Wrong type becomes binding identity |
| B. Last segment | Fails on driver modes, topology, auth, file paths | n/a | none | trivial | Same |
| C. First segment after skipping a declared wrapper/driver list | Fixes every ✅/❌ row; ⚠️ rows unresolved | Matches today's `DbReferenceFactory` behaviour | low | low: one token list in `generic-jdbc.yml` | Vendor-name ≠ type unresolved |
| D. Named-field model (token lookup) | Same coverage as C, plus a clean identity rule and a basis for coordinates (`hosts`, `path`, `storage = mem`) | new | low | medium: vocabulary across ymls, per-engine subname grammar | Vendor-name ≠ type unresolved; token/alias collisions |

## Recommendation

D, limited for ZEE-11955 to the prefix fields plus `hosts`; per-engine subname grammars later. C is the minimal fallback. Neither resolves vendor-name ≠ type (`postgresql`/`postgres`…), which stays an open decision. What would change it: if the vocabulary in yml proves too heavy to maintain, fall back to C.

## Sources
- `connector-commons/connector-commons/src/main/java/zeenea/connector/commons/db/DbReferenceFactory.java:94-179`
- `common-properties/lib/src/main/resources/datasources/*.yml`
- Driver URL formats from vendor JDBC documentation (not re-fetched for this report).
