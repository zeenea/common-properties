import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.*;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

@CacheableTask
public abstract class GenerateCommonPropertiesTask extends DefaultTask {

  private static final int COMMON_PROPERTY_INDENT = 6;
  private static final int ATTRIBUTE_SET_INDENT = 6;
  private static final String YML_INPUT_FILE = "common-properties.yml";
  private static final String COMMON_PROPERTIES_CLASSNAME = "CommonProperties";
  private static final String GENERATED_JAVA_FILE = COMMON_PROPERTIES_CLASSNAME + ".java";

  @InputDirectory
  @PathSensitive(PathSensitivity.RELATIVE)
  public abstract DirectoryProperty getResourcesDir();

  @OutputDirectory
  public abstract DirectoryProperty getOutputDir();

  @TaskAction
  public void generate() throws IOException {
    File resourceFile = getResourcesDir().get().file(YML_INPUT_FILE).getAsFile();
    Path packageDir = getOutputDir().get().getAsFile().toPath().resolve("zeenea/common/properties");
    Files.createDirectories(packageDir);

    List<CommonPropertyParsed> commonProperties = parseFile(resourceFile);

    Files.writeString(
        packageDir.resolve(GENERATED_JAVA_FILE), generateCommonProperties(commonProperties));
    getLogger()
        .lifecycle("Generated {} with {} entries.", GENERATED_JAVA_FILE, commonProperties.size());
  }

  @SuppressWarnings("unchecked")
  private static List<CommonPropertyParsed> parseFile(File file) {
    try (InputStream stream = file.toURI().toURL().openStream()) {
      Map<String, Object> doc = new Yaml().load(stream);

      if (doc == null) return Collections.emptyList();

      List<Map<String, Object>> commonProperties =
          (List<Map<String, Object>>) doc.get("commonProperties");

      return commonProperties.stream()
          .map(
              commonProperty ->
                  new CommonPropertyParsed(
                          getStringOrThrow(commonProperty, "attributeName"),
                          getStringOrThrow(commonProperty, "uuid"),
                          getStringOrThrow(commonProperty, "defaultName"),
                          getStringOrThrow(commonProperty, "defaultDescription"),
                          getStringOrThrow(commonProperty, "machineDescription"),
                          getStringOrThrow(commonProperty, "type"),
                          getBooleanOrThrow(commonProperty, "isPropagable")))
          .collect(Collectors.toList());

    } catch (IOException e) {
      return Collections.emptyList();
    }
  }

  private static boolean getBooleanOrThrow(Map<String, Object> commonProperty, String propertyName) {
    return Boolean.parseBoolean(getOrThrow(commonProperty, propertyName).toString());
  }

  private static @NotNull String getStringOrThrow(Map<String, Object> commonProperty, String propertyName) {
    return getOrThrow(commonProperty, propertyName).toString();
  }

  private static @NotNull Object getOrThrow(Map<String, Object> commonPropertyObj, String key) {
    Object value = commonPropertyObj.get(key);
    if (value == null) {
      throw new IllegalArgumentException("Missing '" + key + "' in object: " + commonPropertyObj);
    }
    return value;
  }

  private static String generateCommonProperties(List<CommonPropertyParsed> commonProperties) {
    StringBuilder sb = new StringBuilder();
    sb.append("package zeenea.common.properties;\n");
    sb.append("\n");
    sb.append("import java.util.Set;\n");
    sb.append("import java.util.UUID;\n");
    sb.append("import zeenea.common.properties.type.Type;\n");
    sb.append("\n");
    sb.append("public class " + COMMON_PROPERTIES_CLASSNAME + " {\n");

    commonProperties.forEach(property -> appendCommonPropertyAttribute(sb, property));

    appendCommonPropertiesSet(commonProperties, sb);

    sb.append("}\n");

    return sb.toString();
  }

  private static void appendCommonPropertiesSet(List<CommonPropertyParsed> commonProperties, StringBuilder sb) {
    sb.append("  public static final Set<CommonProperty> commonProperties =\n");
    sb.append(
        commonProperties.stream()
            .map(s -> indent(s.attributeName, ATTRIBUTE_SET_INDENT + 4))
            .sorted()
            .collect(joining(",\n", indent("Set.of(\n", ATTRIBUTE_SET_INDENT), ");\n")));
  }

  private static void appendCommonPropertyAttribute(StringBuilder sb, CommonPropertyParsed d) {
    sb.append("  public static final CommonProperty " + d.attributeName + " =\n");
    sb.append(
        Stream.of(
                "UUID.fromString(" + encaspulate(d.uuid) + ")",
                encaspulate(d.name),
                encaspulate(d.defaultDescription),
                encaspulate(d.machineDescription),
                "Type." + d.type + "",
                Boolean.toString(d.isPropagable))
            .map(s -> indent(s, COMMON_PROPERTY_INDENT + 4))
            .collect(
                joining(",\n", indent("new CommonProperty(\n", COMMON_PROPERTY_INDENT), ");\n\n")));
  }

  private static @NotNull String indent(String s, int count) {
    return " ".repeat(count) + s;
  }

  private static @NotNull String encaspulate(String value) {
    return "\"" + value + "\"";
  }

  private static class CommonPropertyParsed {
    final String attributeName;
    final String uuid;
    final String name;
    final String defaultDescription;
    final String machineDescription;
    final String type;
    final boolean isPropagable;

    CommonPropertyParsed(
        String attributeName,
        String uuid,
        String name,
        String defaultDescription,
        String machineDescription,
        String type,
        boolean isPropagable) {
      this.attributeName = attributeName;
      this.uuid = uuid;
      this.name = name;
      this.defaultDescription = defaultDescription;
      this.machineDescription = machineDescription;
      this.type = type;
      this.isPropagable = isPropagable;
    }
  }
}
