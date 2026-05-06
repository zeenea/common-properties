package zeenea.common.properties.datasource;

import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class DataSourceTypeGenerationTest {

  private static final String DATA_SOURCE_TYPE_APPROVED = "approval/DataSourceType.approved.java";
  private static final String DATA_SOURCE_TYPE_RECEIVED = "approval/DataSourceType.received.java";

  @Test
  void generatedFileShouldMatchApproved() throws IOException {

    Path generatedFile =
        Paths.get("build/generated/datasource-type")
            .resolve("zeenea/common/properties/datasource/DataSourceType.java");

    String receivedContent = Files.readString(generatedFile, StandardCharsets.UTF_8);
    String approvedContent = readApproved();

    if (!receivedContent.equals(approvedContent)) {

      Path receivedPath = receivedPath();
      Files.writeString(receivedPath, receivedContent, StandardCharsets.UTF_8);
      fail(
          "Generated DataSourceType.java does not match approved snapshot.\n"
              + "Received file written to: %s\n"
              + "To approve, run: cp %s %s",
          receivedPath, receivedPath, approvedPath());
    }
  }

  private Path receivedPath() {
    return Paths.get("src/test/resources").resolve(DATA_SOURCE_TYPE_RECEIVED);
  }

  private Path approvedPath() {
    return Paths.get("src/test/resources").resolve(DATA_SOURCE_TYPE_APPROVED);
  }

  private static String readApproved() throws IOException {
    try (InputStream stream =
        DataSourceTypeGenerationTest.class
            .getClassLoader()
            .getResourceAsStream(DATA_SOURCE_TYPE_APPROVED)) {

      if (stream == null) {
        return "";
      }

      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}
