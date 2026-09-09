package zeenea.common.properties;

import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class CommonPropertiesGenerationTest {

  private static final String COMMON_PROPERTIES_APPROVED =
      "approval/CommonProperties.approved.java";
  private static final String COMMON_PROPERTIES_RECEIVED =
      "approval/CommonProperties.received.java";

  @Test
  void generatedFileShouldMatchApproved() throws IOException {

    Path generatedFile =
        Paths.get("build/generated/common-properties")
            .resolve("zeenea/common/properties/CommonProperties.java");

    String receivedContent = Files.readString(generatedFile, StandardCharsets.UTF_8);
    String approvedContent = readApproved();

    if (!receivedContent.equals(approvedContent)) {

      Path receivedPath = receivedPath();
      Files.writeString(receivedPath, receivedContent, StandardCharsets.UTF_8);
      fail(
          "Generated CommonProperties.java does not match approved snapshot.\n"
              + "Received file written to: %s\n"
              + "To approve, run: cp %s %s",
          receivedPath, receivedPath, approvedPath());
    }
  }

  private Path receivedPath() {
    return Paths.get("src/test/resources").resolve(COMMON_PROPERTIES_RECEIVED);
  }

  private Path approvedPath() {
    return Paths.get("src/test/resources").resolve(COMMON_PROPERTIES_APPROVED);
  }

  private static String readApproved() throws IOException {
    try (InputStream stream =
        CommonPropertiesGenerationTest.class
            .getClassLoader()
            .getResourceAsStream(COMMON_PROPERTIES_APPROVED)) {

      if (stream == null) {
        return "";
      }

      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    }
  }
}
