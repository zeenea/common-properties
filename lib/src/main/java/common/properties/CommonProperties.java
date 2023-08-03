package common.properties;

import java.util.Set;
import java.util.UUID;

public record CommonProperties(
    UUID uuid,
    String code,
    String default_name,
    String default_description,
    Type type,
    Set<UUID> uuids) {}
