package dev.rkoch.uhx.shared.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public final class ResourceUtil {

  private ResourceUtil() {

  }

  public static String readString(final String resourceName) {
    try {
      return Files.readString(Paths
          .get(Thread.currentThread().getContextClassLoader().getResource(resourceName).toURI()));
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<String> readAllLines(final String resourceName) {
    try {
      return Files.readAllLines(Paths
          .get(Thread.currentThread().getContextClassLoader().getResource(resourceName).toURI()));
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}
