# common-properties

This project contains the common properties shared between connectors. It is used by the Datacatalog to identify them.

## Overview

This project mainly consists in defining Types available from common properties, and the one currently available are listed in CommonProperties.java.

It lists also how to identify uniquely a DataSource inside Zeenea Datacatalog.

You're not supposed to use it directly, it's only useful for the public SDK which internally uses it.

### Local publication

In order to reference this package locally from other repositories (like [public-connector-sdk][public-connector-sdk]), one can run:

```shell
./gradlew publishToMavenLocal
```

[public-connector-sdk]: https://github.com/zeenea/public-connector-sdk
