package dev.rkoch.uhx.shared.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoaderUtil {

  private static final HttpClient CLIENT = HttpClient.newHttpClient();

  public static byte[] getDataFromUrl(final String url) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
    return CLIENT.send(request, BodyHandlers.ofByteArray()).body();
  }

  public static Path getPathToResource(final String resource) throws URISyntaxException {
    return Paths.get(Thread.currentThread().getContextClassLoader().getResource("").toURI())
        .resolve("../../src/main/resources").resolve(resource);
  }

  public static URL getUrl(final String url) {
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

}
