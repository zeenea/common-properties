package zeenea.common.properties;

import java.util.Set;
import java.util.UUID;
import zeenea.common.properties.type.Type;

public class CommonProperties {
  public static final CommonProperty catalogCommonProperty =
      new CommonProperty(
          UUID.fromString("610b8e06-7fcb-4de6-b1a6-594b05901463"),
          "Catalog",
          "Common Property Catalog",
          "Source system catalog storing the item. In some source systems, \"database\" is replaced by catalog.",
          Type.TAG,
          true);

  public static final CommonProperty databaseCommonProperty =
      new CommonProperty(
          UUID.fromString("2e3ac846-79b5-4953-8b76-a0cbe794453b"),
          "Database",
          "Common Property Database",
          "Name or identifier of the Database storing the item",
          Type.TAG,
          true);

  public static final CommonProperty schemaCommonProperty =
      new CommonProperty(
          UUID.fromString("7055eb42-329e-11ee-a1c1-af9eec72241a"),
          "Schema",
          "Common Property Schema",
          "Name or identifier of the Schema (Database schema, etc) storing the item",
          Type.TAG,
          true);

  public static final CommonProperty typeCommonProperty =
      new CommonProperty(
          UUID.fromString("cbfe45a3-986f-4b7b-bfc2-23a022a31012"),
          "Type",
          "Common Property Type",
          "Item type from source system",
          Type.TAG,
          false);

  public static final CommonProperty sqlQueryProperty =
      new CommonProperty(
          UUID.fromString("18c61716-e110-40c1-8ca2-3a1468842a4a"),
          "SQL Query",
          "SQL Queries executed by the process",
          "SQL query associated with a process or a dataset",
          Type.LONG_TEXT,
          false);

  public static final CommonProperty sqlQueryDialectProperty =
      new CommonProperty(
          UUID.fromString("b94ab627-9b06-4866-b304-47028baf5608"),
          "SQL Query dialect",
          "Dialect of SQL Queries executed by the process",
          "Dialect (ANSI, T-SQL, etc) of an SQL query executed by a process or a view",
          Type.TAG,
          false);

  public static final CommonProperty accessUrlProperty =
      new CommonProperty(
          UUID.fromString("08bfb9ee-b161-11ee-9cf6-1f4507e5336f"),
          "Access URL",
          "Common Property Access URL",
          "URL to access the item in the source system",
          Type.URL,
          false);

  public static final CommonProperty expressionProperty =
      new CommonProperty(
          UUID.fromString("2188c4bc-fdc5-43a6-a895-89afd9d28f81"),
          "Expression",
          "Field expression",
          "Calculated field expression description",
          Type.LONG_TEXT,
          false);

  public static final CommonProperty powerQueryProperty =
      new CommonProperty(
          UUID.fromString("8691460c-a2b8-4f59-970f-fd3f026a6071"),
          "Power Query",
          "Power Query",
          "PowerQuery associated with PowerBI datasets from semantic model",
          Type.LONG_TEXT,
          false);

  public static final CommonProperty dataSourceTypeProperty =
      new CommonProperty(
          UUID.fromString("68d7f77d-06c2-4772-91cc-877f7eb8bc08"),
          "Datasource Type",
          "Common Property Datasource Type",
          "Entity type in the data source",
          Type.TAG,
          false);

  public static final Set<CommonProperty> commonProperties =
      Set.of(
          accessUrlProperty,
          catalogCommonProperty,
          databaseCommonProperty,
          schemaCommonProperty,
          typeCommonProperty,
          sqlQueryProperty,
          sqlQueryDialectProperty,
          expressionProperty,
          powerQueryProperty,
          dataSourceTypeProperty);
}
