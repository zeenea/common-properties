package zeenea.common.properties.datasource;

import static zeenea.common.properties.datasource.DataSourceIdentifierKeys.*;

import java.util.Set;

public enum DataSourceIdentifier {
  CUSTOM {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of();
    }
  },

  ATHENA {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(SCHEMA, STAGING_DIRECTORY, CATALOG, REGION_NAME);
    }
  },

  AZURE {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(LOCATION, FORMAT, DELIMITER);
    }
  },

  BIGQUERY {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(PROJECT, DATASET);
    }
  },

  DATABRICKS {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(CATALOG, SCHEMA, HOST);
    }
  },

  DB2 {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, PORT, DATABASE, SCHEMA);
    }
  },

  DENODO {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, PORT, DATABASE);
    }
  },

  GLUE {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(ACCOUNT, DATABASE, LOCATION, FORMAT);
    }
  },

  INFORMIX {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, PORT, DATABASE);
    }
  },

  KAFKA {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, FORMAT);
    }
  },

  LOCAL {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(PATH, FORMAT);
    }
  },

  MYSQL {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, PORT, DATABASE);
    }
  },

  ORACLE {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, PORT, SERVICE_NAME);
    }
  },

  POSTGRESQL {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, PORT, DATABASE, SCHEMA);
    }
  },

  REDSHIFT {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(DATABASE, SCHEMA, HOST, REGION, ACCOUNT);
    }
  },

  S3 {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(BUCKET);
    }
  },

  SNOWFLAKE {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, PORT, ACCOUNT, DATABASE, WAREHOUSE, SCHEMA);
    }
  },

  SQLSERVER {
    @Override
    public Set<DataSourceIdentifierKey> getMatchingKeys() {
      return Set.of(HOST, PORT, DATABASE, SCHEMA);
    }
  };

  public abstract Set<DataSourceIdentifierKey> getMatchingKeys();
}
