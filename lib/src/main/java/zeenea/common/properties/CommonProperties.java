package zeenea.common.properties;

import java.util.Set;
import java.util.UUID;
import zeenea.common.properties.type.Type;

public class CommonProperties {
  public static final CommonProperty schemaCommonProperty =
      new CommonProperty(
          UUID.fromString("7055eb42-329e-11ee-a1c1-af9eec72241a"),
          "$z_schema",
          "Schema",
          "Common Property Schema",
          Type.TAG,
          Set.of(
              UUID.fromString("74283f47-aa3b-4dcb-b41b-c49dce8433cb"),
              UUID.fromString("7f24669b-7bb1-4dcd-a434-bf1bb3ef66cb"),
              UUID.fromString("de41f160-3351-3020-be4b-a33b56d433b2"),
              UUID.fromString("36dd4563-b1cf-4df1-a443-fb5c2c645466"),
              UUID.fromString("f5ffe64e-6646-498e-90a1-87387f22a647")));

  public static final Set<CommonProperty> commonProperties = Set.of(schemaCommonProperty);
}
