package zeenea.common.properties;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import zeenea.common.properties.type.Type;

public final class CommonProperty {
  private final UUID uuid;

  /**
   * The default name for users in the UI. This name can be overridden by users in the Catalog
   * Design in the Studio.
   */
  private final String defaultName;

  /**
   * The default description for users in the UI. This description can be overridden by users in the
   * Catalog Design in the Studio.
   */
  @Nullable private final String defaultDescription;

  /**
   * The description targeting machines. This description aims to be used by LLMs and is not
   * modifiable by users. It is not displayed in the Webapps.
   */
  @Nullable private final String machineDescription;

  private final Type type;

  private final Boolean isPropagable;

  public CommonProperty(
      UUID uuid,
      String defaultName,
      @Nullable String defaultDescription,
      @Nullable String machineDescription,
      Type type,
      Boolean isPropagable) {
    this.uuid = uuid;
    this.defaultName = defaultName;
    this.defaultDescription = defaultDescription;
    this.machineDescription = machineDescription;
    this.type = type;
    this.isPropagable = isPropagable;
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

  @Nullable
  public String machineDescription() {
    return machineDescription;
  }

  public Optional<String> getMachineDescription() {
    return Optional.ofNullable(machineDescription);
  }

  public Type type() {
    return type;
  }

  public Boolean getIsPropagable() {
    return isPropagable;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (CommonProperty) obj;
    return Objects.equals(this.uuid, that.uuid)
        && Objects.equals(this.defaultName, that.defaultName)
        && Objects.equals(this.defaultDescription, that.defaultDescription)
        && Objects.equals(this.machineDescription, that.machineDescription)
        && Objects.equals(this.type, that.type)
        && Objects.equals(this.isPropagable, that.isPropagable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        uuid, defaultName, defaultDescription, machineDescription, type, isPropagable);
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
        + "machineDescription="
        + machineDescription
        + ", "
        + "type="
        + type
        + ", "
        + "isPropagable="
        + isPropagable
        + ']';
  }
}
