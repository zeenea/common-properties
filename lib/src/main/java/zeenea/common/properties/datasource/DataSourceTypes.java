package zeenea.common.properties.datasource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class DataSourceTypes {

  private static final String DIRECTORY = "datasources";
  private static final Map<String, List<String>> KEYS;
  private static final String TYPE = "type";

  private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());

  private static final Configuration JSONPATH_CONF =
      Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();

  static {
    KEYS = loadDirectory();
  }

  private DataSourceTypes() {}

  public static List<String> getMatchingKeys(String type) {
    return KEYS.getOrDefault(type.toLowerCase(), List.of());
  }

  private static Map<String, List<String>> loadDirectory() {
    URL dirUrl = DataSourceTypes.class.getClassLoader().getResource(DIRECTORY);
    if (dirUrl == null || !"file".equals(dirUrl.getProtocol())) return Collections.emptyMap();

    try {
      File dir = new File(dirUrl.toURI());
      File[] files = dir.listFiles((d, name) -> name.endsWith(".yml"));
      if (files == null) return Collections.emptyMap();

      return Arrays.stream(files)
          .map(File::getName)
          .map(DataSourceTypes::parseFile)
          .flatMap(Optional::stream)
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    } catch (URISyntaxException e) {
      return Collections.emptyMap();
    }
  }

  private static Optional<Map.Entry<String, List<String>>> parseFile(String fileName) {
    URL fileUrl = DataSourceTypes.class.getClassLoader().getResource(DIRECTORY + "/" + fileName);
    if (fileUrl == null) return Optional.empty();

    try (InputStream stream = fileUrl.openStream()) {
      ReadContext ctx =
          JsonPath.using(JSONPATH_CONF).parse(YAML_MAPPER.readValue(stream, Map.class));

      String type = ctx.read("$.datasource.values.type");
      if (type == null || type.isBlank()) return Optional.empty();

      List<String> allKeys = ctx.read("$.datasource.keys[*]");

      return Optional.of(
          Map.entry(
              type,
              allKeys.stream()
                  .filter(s -> !TYPE.equalsIgnoreCase(s))
                  .collect(Collectors.toList())));
    } catch (IOException e) {
      return Optional.empty();
    }
  }
}
