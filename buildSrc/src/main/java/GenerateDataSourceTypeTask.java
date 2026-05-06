import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.yaml.snakeyaml.Yaml;

@CacheableTask
public abstract class GenerateDataSourceTypeTask extends DefaultTask {

  @InputDirectory
  @PathSensitive(PathSensitivity.RELATIVE)
  public abstract DirectoryProperty getResourcesDir();

  @OutputDirectory
  public abstract DirectoryProperty getOutputDir();

  @TaskAction
  public void generate() throws IOException {
    File resourcesDir = getResourcesDir().get().getAsFile();
    Path packageDir =
        getOutputDir()
            .get()
            .getAsFile()
            .toPath()
            .resolve("zeenea/common/properties/datasource");
    Files.createDirectories(packageDir);

    List<EnumEntry> entries = parseEntries(resourcesDir);
    Files.writeString(packageDir.resolve("DataSourceType.java"), generateEnum(entries));
    getLogger().lifecycle("Generated DataSourceType.java with {} entries.", entries.size());
  }

  private static List<EnumEntry> parseEntries(File resourcesDir) {
    File[] files = resourcesDir.listFiles((d, name) -> name.endsWith(".yml"));
    if (files == null) return Collections.emptyList();

    return Arrays.stream(files)
        .map(GenerateDataSourceTypeTask::parseFile)
        .flatMap(Optional::stream)
        .sorted(Comparator.comparing(e -> e.enumName))
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private static Optional<EnumEntry> parseFile(File file) {
    try (InputStream stream = file.toURI().toURL().openStream()) {
      Map<String, Object> doc = new Yaml().load(stream);
      if (doc == null) return Optional.empty();

      Map<String, Object> datasource = (Map<String, Object>) doc.get("datasource");
      if (datasource == null) return Optional.empty();

      Map<String, Object> values = (Map<String, Object>) datasource.get("values");
      if (values == null) return Optional.empty();

      Object typeValue = values.get("type");
      if (!(typeValue instanceof String)) return Optional.empty();
      String type = (String) typeValue;

      List<String> allKeys = (List<String>) datasource.getOrDefault("keys", Collections.emptyList());
      List<String> keys =
          allKeys.stream().filter(k -> !k.equals("type")).collect(Collectors.toList());

      return Optional.of(new EnumEntry(toUpperSnakeCase(type), keys));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  private static String toUpperSnakeCase(String type) {
    return type.replaceAll("[^a-zA-Z0-9]", "_").toUpperCase();
  }

  private static String generateEnum(List<EnumEntry> entries) {
    StringBuilder sb = new StringBuilder();
    sb.append("package zeenea.common.properties.datasource;\n\n");
    sb.append("import java.util.List;\n\n");
    sb.append("public enum DataSourceType {\n");

    for (int i = 0; i < entries.size(); i++) {
      EnumEntry entry = entries.get(i);
      sb.append("  ").append(entry.enumName);
      if (!entry.keys.isEmpty()) {
        String keysStr =
            entry.keys.stream().map(k -> "\"" + k + "\"").collect(Collectors.joining(", "));
        sb.append("(").append(keysStr).append(")");
      }
      sb.append(i < entries.size() - 1 ? "," : ";").append("\n");
    }

    sb.append("\n");
    sb.append("  private final List<String> matchingKeys;\n\n");
    sb.append("  DataSourceType(String... matchingKeys) {\n");
    sb.append("    this.matchingKeys = List.of(matchingKeys);\n");
    sb.append("  }\n\n");
    sb.append("  public List<String> getMatchingKeys() {\n");
    sb.append("    return matchingKeys;\n");
    sb.append("  }\n");
    sb.append("}\n");

    return sb.toString();
  }

  private static class EnumEntry {
    final String enumName;
    final List<String> keys;

    EnumEntry(String enumName, List<String> keys) {
      this.enumName = enumName;
      this.keys = keys;
    }
  }
}
