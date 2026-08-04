import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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

  // Number of columns, beyond which google-java-format wraps a line
  private static final int GJF_COLUMN_LIMIT = 100;

  // Spaces that google-java-format adds per continuation / nesting level
  private static final int GJF_CONTINUATION_INDENT = 4;

  // Indentation at which each "Map.entry(...)" sits inside "Map.ofEntries(...)"
  private static final int MAP_ENTRY_INDENT = 10;

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

    List<Datasource> datasources = parse(resourcesDir);

    Files.writeString(
        packageDir.resolve("DataSourceType.java"), generateDataSourceType(datasources));
    getLogger().lifecycle("Generated DataSourceType.java with {} entries.", datasources.size());

    List<Datasource> datasourcesWithDatasets =
        datasources.stream()
            .filter(d -> !d.datasetIdentificationKeys.isEmpty())
            .collect(Collectors.toList());
    Files.writeString(
        packageDir.resolve("DatasetIdentificationKeys.java"),
        generateDatasetIdentificationKeys(datasourcesWithDatasets));
    getLogger()
        .lifecycle(
            "Generated DatasetIdentificationKeys.java with {} entries.", datasourcesWithDatasets.size());
  }

  private static List<Datasource> parse(File resourcesDir) {
    File[] files = resourcesDir.listFiles((d, name) -> name.endsWith(".yml"));
    if (files == null) return Collections.emptyList();

    return Arrays.stream(files)
        .map(GenerateDataSourceTypeTask::parseFile)
        .flatMap(Optional::stream)
        .sorted(Comparator.comparing(d -> d.enumName))
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private static Optional<Datasource> parseFile(File file) {
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
      List<String> datasourceKeys =
          allKeys.stream().filter(k -> !k.equals("type")).collect(Collectors.toList());

      List<List<String>> datasetIdentificationKeys = parseDatasetIdentificationKeys(doc);

      return Optional.of(new Datasource(toUpperSnakeCase(type), datasourceKeys, datasetIdentificationKeys));
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  @SuppressWarnings("unchecked")
  private static List<List<String>> parseDatasetIdentificationKeys(Map<String, Object> doc) {
    Object datasetsValue = doc.get("datasets");
    if (!(datasetsValue instanceof List)) return Collections.emptyList();

    List<List<String>> orders = new ArrayList<>();
    for (Object datasetValue : (List<Object>) datasetsValue) {
      if (!(datasetValue instanceof Map)) continue;
      Object identificationValue = ((Map<String, Object>) datasetValue).get("identification");
      if (!(identificationValue instanceof Map)) continue;
      Object keysValue = ((Map<String, Object>) identificationValue).get("keys");
      if (keysValue instanceof List) {
        orders.add(new ArrayList<>((List<String>) keysValue));
      }
    }

    orders.sort(Comparator.comparingInt((List<String> keys) -> keys.size()).reversed());
    return orders.stream().distinct().collect(Collectors.toList());
  }

  private static String toUpperSnakeCase(String type) {
    return type.replaceAll("[^a-zA-Z0-9]", "_").toUpperCase();
  }

  private static String generateDataSourceType(List<Datasource> datasources) {
    StringBuilder sb = new StringBuilder();
    sb.append("package zeenea.common.properties.datasource;\n\n");
    sb.append("import java.util.List;\n\n");
    sb.append("public enum DataSourceType {\n");

    for (int i = 0; i < datasources.size(); i++) {
      Datasource d = datasources.get(i);
      sb.append("  ").append(d.enumName);
      if (!d.datasourceKeys.isEmpty()) {
        String keysStr =
            d.datasourceKeys.stream().map(k -> "\"" + k + "\"").collect(Collectors.joining(", "));
        sb.append("(").append(keysStr).append(")");
      }
      sb.append(i < datasources.size() - 1 ? "," : ";").append("\n");
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

  private static String generateDatasetIdentificationKeys(List<Datasource> datasources) {
    StringBuilder sb = new StringBuilder();
    sb.append("package zeenea.common.properties.datasource;\n\n");
    sb.append("import java.util.List;\n");
    sb.append("import java.util.Map;\n\n");
    sb.append("public final class DatasetIdentificationKeys {\n\n");
    sb.append(
        "  public static final Map<DataSourceType, List<List<String>>> ACCEPTED_KEY_ORDERS =\n");
    sb.append("      Map.ofEntries(\n");

    for (int i = 0; i < datasources.size(); i++) {
      sb.append(renderEntry(datasources.get(i)));
      sb.append(i < datasources.size() - 1 ? ",\n" : ");\n");
    }

    sb.append("\n");
    sb.append("  private DatasetIdentificationKeys() {}\n");
    sb.append("}\n");

    return sb.toString();
  }

  private static String renderEntry(Datasource d) {
    String typeRef = "DataSourceType.%s".formatted(d.enumName);
    String inline =
        "Map.entry(%s, %s)".formatted(typeRef, renderOrders(d.datasetIdentificationKeys));
    if (MAP_ENTRY_INDENT + inline.length() + 1 <= GJF_COLUMN_LIMIT) {
      return " ".repeat(MAP_ENTRY_INDENT) + inline;
    }
    String entryIndent = " ".repeat(MAP_ENTRY_INDENT);
    int argIndentWidth = MAP_ENTRY_INDENT + GJF_CONTINUATION_INDENT;
    String argIndent = " ".repeat(argIndentWidth);
    return """
        %sMap.entry(
        %s%s,
        %s%s)"""
        .formatted(
            entryIndent,
            argIndent,
            typeRef,
            argIndent,
            renderOrders(d.datasetIdentificationKeys, argIndentWidth));
  }

  private static String renderOrders(List<List<String>> keyOrders) {
    return "List.of(%s)"
        .formatted(
            keyOrders.stream().map(GenerateDataSourceTypeTask::renderOrder).collect(joining(", ")));
  }

  private static String renderOrders(List<List<String>> keyOrders, int indent) {
    String inline = renderOrders(keyOrders);
    if (indent + inline.length() + 2 <= GJF_COLUMN_LIMIT) {
      return inline;
    }
    String elementIndent = " ".repeat(indent + GJF_CONTINUATION_INDENT);
    String elements =
        keyOrders.stream().map(order -> elementIndent + renderOrder(order)).collect(joining(",\n"));
    return """
        List.of(
        %s)""".formatted(elements);
  }

  private static String renderOrder(List<String> order) {
    return "List.of(%s)".formatted(order.stream().map(k -> "\"" + k + "\"").collect(joining(", ")));
  }

  private static class Datasource {
    final String enumName;
    final List<String> datasourceKeys;
    final List<List<String>> datasetIdentificationKeys;

    Datasource(String enumName, List<String> datasourceKeys, List<List<String>> datasetIdentificationKeys) {
      this.enumName = enumName;
      this.datasourceKeys = datasourceKeys;
      this.datasetIdentificationKeys = datasetIdentificationKeys;
    }
  }
}
