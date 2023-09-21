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
          Type.TAG,
          true);

  public static final CommonProperty databaseCommonProperty =
      new CommonProperty(
          UUID.fromString("2e3ac846-79b5-4953-8b76-a0cbe794453b"),
          "Database",
          "Common Property Database",
          Type.TAG,
          true);
  public static final CommonProperty schemaCommonProperty =
      new CommonProperty(
          UUID.fromString("7055eb42-329e-11ee-a1c1-af9eec72241a"),
          "Schema",
          "Common Property Schema",
          Type.TAG,
          true);

  public static final CommonProperty typeCommonProperty =
      new CommonProperty(
          UUID.fromString("cbfe45a3-986f-4b7b-bfc2-23a022a31012"),
          "Type",
          "Common Property Type",
          Type.TAG,
          false);

  public static final Set<CommonProperty> commonProperties =
      Set.of(
          catalogCommonProperty, databaseCommonProperty, schemaCommonProperty, typeCommonProperty);
}
