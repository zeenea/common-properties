package zeenea.common.properties;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import zeenea.common.properties.type.Type;

public final class CommonProperty {
  private final UUID uuid;
  private final String defaultName;
  @Nullable private final String defaultDescription;
  private final Type type;

  public CommonProperty(
      UUID uuid, String defaultName, @Nullable String defaultDescription, Type type) {
    this.uuid = uuid;
    this.defaultName = defaultName;
    this.defaultDescription = defaultDescription;
    this.type = type;
  }

  public UUID uuid() {
    return uuid;
  }

  public String defaultName() {
    return defaultName;
  }

  @Nullable
  public String defaultDescription() {
    return defaultDescription;
  }

  public Optional<String> getDefaultDescription() {
    return Optional.ofNullable(defaultDescription);
  }

  public Type type() {
    return type;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (CommonProperty) obj;
    return Objects.equals(this.uuid, that.uuid)
        && Objects.equals(this.defaultName, that.defaultName)
        && Objects.equals(this.defaultDescription, that.defaultDescription)
        && Objects.equals(this.type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, defaultName, defaultDescription, type);
  }

  @Override
  public String toString() {
    return "CommonProperty["
        + "uuid="
        + uuid
        + ", "
        + "defaultName="
        + defaultName
        + ", "
        + "defaultDescription="
        + defaultDescription
        + ", "
        + "type="
        + type
        + ']';
  }
}
