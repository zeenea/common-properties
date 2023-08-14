package zeenea.common.properties;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import zeenea.common.properties.type.Type;

public final class CommonProperty {
  private final UUID uuid;
  private final String code;
  private final String defaultName;
  @Nullable private final String defaultDescription;
  private final Type type;
  private final Set<UUID> relatedUuids;

  public CommonProperty(
      UUID uuid,
      String code,
      String defaultName,
      @Nullable String defaultDescription,
      Type type,
      Set<UUID> relatedUuids) {
    this.uuid = uuid;
    this.code = code;
    this.defaultName = defaultName;
    this.defaultDescription = defaultDescription;
    this.type = type;
    this.relatedUuids = Set.copyOf(relatedUuids);
  }

  public UUID uuid() {
    return uuid;
  }

  public String code() {
    return code;
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

  public Set<UUID> relatedUuids() {
    return relatedUuids;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (CommonProperty) obj;
    return Objects.equals(this.uuid, that.uuid)
        && Objects.equals(this.code, that.code)
        && Objects.equals(this.defaultName, that.defaultName)
        && Objects.equals(this.defaultDescription, that.defaultDescription)
        && Objects.equals(this.type, that.type)
        && Objects.equals(this.relatedUuids, that.relatedUuids);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid, code, defaultName, defaultDescription, type, relatedUuids);
  }

  @Override
  public String toString() {
    return "CommonProperty["
        + "uuid="
        + uuid
        + ", "
        + "code="
        + code
        + ", "
        + "defaultName="
        + defaultName
        + ", "
        + "defaultDescription="
        + defaultDescription
        + ", "
        + "type="
        + type
        + ", "
        + "relatedUuids="
        + relatedUuids
        + ']';
  }
}
