package dev.rkoch.uhx.vienna.direction;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QuandoFpaServiceClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuandoFpaServiceClient.class);

  private static final HttpClient CLIENT = HttpClient.newHttpClient();

  private static final ZoneId ZONE_ID_VIENNA = ZoneId.of("Europe/Vienna");

  // not used because slower than local lookup
  // https://www.wienerlinien.at/qando-fpa/location?search=<search_text>

  private static final String URL = "https://www.wienerlinien.at/qando-fpa/route?" + //
      "aPP=1" + //
      "&aPD=1" + //
      "&aTI=1" + //
      "&aflT=1" + //
      "&walkMT=15" + //
      "&version=1.1" + //
      "&ptRO=ptMinWalk" + // route option: ptMinTime|ptMinChanges|ptMinWalk
      "&ptMC=-1" + // move count: -1|0|1|2
      "&ptWS=ptNormal" + // walking speed: ptNormal|ptSlow|ptFast
      "&ptV=ptTrainR" + //
      "&ptV=ptTrainS" + //
      "&ptV=ptMetro" + //
      "&ptV=ptTram" + //
      "&ptV=ptAirportBus" + //
      "&ptV=ptTrainCAT" + //
      "&ptV=ptBusCity" + //
      "&ptV=ptBusNight" + //
      "&ptV=ptBusRegion" + //
      "&ptV=ptTaxi" + //
      "&ptMWT=30";

  private QuandoFpaServiceClient() {

  }

  public static void main(String[] args) {
    getDirection("60200819", "60201320");
    long time = System.currentTimeMillis();
    // System.out.println(getDirection("60200819", "60201320"));
    // System.out.println(getDirection("60200949", "60200491"));
    // System.out.println(getDirection("60201326", "60203510"));
    // System.out.println(getDirection("60203510", "60200491"));
    System.out.println(getDirection("60200949", "60201326"));
    System.out.println("duration: " + (System.currentTimeMillis() - time));
  }

  private static String getEncodedDate(final ZonedDateTime zonedDateTime) {
    return URLEncoder.encode(zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
        StandardCharsets.UTF_8);
  }

  public static Optional<String> getDirection(final String originDiva,
      final String destinationDiva) {
    return getDirection(Optional.of(originDiva), Optional.of(destinationDiva), Optional.empty());
  }

  public static Optional<String> getDirection(final Optional<String> originDiva,
      final Optional<String> destinationDiva) {
    return getDirection(originDiva, destinationDiva, Optional.empty());
  }

  private static String getDynamicQuery(final String originDiva, final String destinationDiva,
      final Optional<ZonedDateTime> arrivalDateTime) {
    StringBuilder builder = new StringBuilder();
    builder.append("&deparr=");
    if (arrivalDateTime.isPresent()) {
      builder.append("arr");
    } else {
      builder.append("dep");
    }
    builder.append("&date=");
    if (arrivalDateTime.isPresent()) {
      builder.append(getEncodedDate(arrivalDateTime.get()));
    } else {
      builder.append(getEncodedDate(ZonedDateTime.now(ZONE_ID_VIENNA)));
    }
    builder.append("&from=").append(originDiva);
    builder.append("&to=").append(destinationDiva);
    return builder.toString();
  }

  public static Optional<String> getDirection(final Optional<String> originDiva,
      final Optional<String> destinationDiva, final Optional<ZonedDateTime> arrivalDateTime) {
    if (originDiva.isPresent() && destinationDiva.isPresent()) {
      String routeInfo = getRouteInfo(originDiva.get(), destinationDiva.get(), arrivalDateTime);
      System.out.println(routeInfo);
      return QuandoFpaDataProcessor.getRouteText(routeInfo);
    } else {
      return Optional.empty();
    }
  }

  private static String getRouteInfo(final String originDiva, final String destinationDiva,
      final Optional<ZonedDateTime> arrivalDateTime) {
    try {
      URI uri = URI.create(URL + getDynamicQuery(originDiva, destinationDiva, arrivalDateTime));
      HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
      return CLIENT.send(request, BodyHandlers.ofString()).body();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

}
