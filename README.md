# common-properties

This project contains the common properties shared between connectors. It is used by the Datacatalog to identify them.

## Overview

This project mainly consists in defining Types available from common properties, and the one currently available are listed in CommonProperties.java.

It lists also how to identify uniquely a DataSource inside Zeenea Datacatalog.

You're not supposed to use it directly, it's only useful for the public SDK which internally uses it.

### Keep calm and...

The [data-sources](lib/src/main/resources/datasources) folder is the single source of truth for all connectors.
The yaml files in there are very sensitive to changes, all the references are based on the identifiers defined here.
/!\ "Fixing a typo" in a .yml is likely to break lineage, think twice then... /!\

If the library build fails, it's likely that you added/removed a datasource in there or moved one from draft-datasources in here.
If you are sure about what you are doing, 
you can replace [DataSourceType.approved](lib/src/test/resources/approval/DataSourceType.approved.java) content 
with [DataSourceType.received](lib/src/test/resources/approval/DataSourceType.received.java) content


### Local publication

In order to reference this package locally from other repositories (like [public-connector-sdk][public-connector-sdk]), one can run:

```shell
./gradlew publishToMavenLocal
```



[public-connector-sdk]: https://github.com/zeenea/public-connector-sdk
