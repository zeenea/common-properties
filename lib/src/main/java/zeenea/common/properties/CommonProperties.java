package zeenea.common.properties;

import java.util.Set;
import java.util.UUID;
import zeenea.common.properties.type.Type;

public class CommonProperties {
  public static final CommonProperty schemaCommonProperty =
      new CommonProperty(
          UUID.fromString("7055eb42-329e-11ee-a1c1-af9eec72241a"),
          "Schema",
          "Common Property Schema",
          Type.TAG,
          true);

  public static final Set<CommonProperty> commonProperties = Set.of(schemaCommonProperty);
}
