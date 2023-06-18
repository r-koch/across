package dev.rkoch.uhx.vienna.direction;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class QuandoFpaDataProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuandoFpaDataProcessor.class);

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private QuandoFpaDataProcessor() {

  }

  public static Optional<String> getRouteText(final String routeInfo) {
    JsonNode data = null;
    try {
      data = MAPPER.readTree(routeInfo).findValue("data");
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Optional.empty();
    }
    if (data != null && !data.isEmpty()) {
      StringBuilder builder = new StringBuilder();
      String from = data.findValue("locationFrom").findValue("title").asText();
      String to = data.findValue("locationTo").findValue("title").asText();
      builder.append("Von ");
      builder.append(from);
      builder.append(" nach ");
      builder.append(to);
      builder.append(": ");
      JsonNode trip = data.findValue("trips").get(1);
      builder.append(getSegmentsText(trip.findValue("segments")));
      builder.append(". Weegzeit ");
      builder.append(trip.findValue("durationMinutes").asText());
      builder.append(" Minuten.");
      return Optional.of(builder.toString());
    } else {
      LOGGER.error("data is null of empty");
      return Optional.empty();
    }
  }

  private static String getSegmentsText(final JsonNode segments) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < segments.size(); i++) {
      JsonNode segment = segments.get(i);
      JsonNode vehicle = segment.findValue("vehicle");
      String vehicleType = vehicle.findValue("type").asText();
      switch (vehicleType) {
        case "change":
          builder.append(", umsteigen zu ");
          break;

        case "walk":
          if (i != 0) {
            builder.append(", ");
          }
          builder.append(vehicleTypeToText(vehicleType));
          builder.append(" ");
          if (i == 0) {
            builder.append("von ");
            builder.append(
                cleanStationName(segment.findValue("locationFrom").findValue("title").asText()));
            builder.append(" ");
          }
          builder.append("bis ");
          builder.append(
              cleanStationName(segment.findValue("locationTo").findValue("title").asText()));
          if (i != segments.size() - 1)
            builder.append(", dann mit ");
          break;

        case "ptTrainR":
        case "ptTrainS":
        case "ptMetro":
        case "ptTram":
        case "ptAirportBus":
        case "ptTrainCAT":
        case "ptBusCity":
        case "ptBusNight":
        case "ptBusRegion":
        case "ptTaxi":
          if (i == 0) {
            builder.append("mit ");
          }
          builder.append(vehicleTypeToText(vehicleType));
          builder.append(" ");
          builder.append(vehicle.findValue("name").asText());
          builder.append(" ");

          if (i == 0) {
            builder.append("von ");
            builder.append(
                cleanStationName(segment.findValue("locationFrom").findValue("title").asText()));
            builder.append(" ");
          }
          builder.append("bis ");
          builder.append(
              cleanStationName(segment.findValue("locationTo").findValue("title").asText()));
          break;

        default:
          throw new RuntimeException("unhandled vehicle type: " + vehicleType);
      }

    }
    return builder.toString();
  }

  private static String cleanStationName(final String stationName) {
    return stationName.replace("Wien ", "").replaceAll(" U$", "");
  }

  private static String vehicleTypeToText(final String vehicleType) {
    switch (vehicleType) {
      case "ptTrainR":
        return "Regionalzug";
      case "ptTrainS":
        return "Schnellbahn";
      case "ptMetro":
        return "U-Bahn";
      case "ptTram":
        return "Straßenbahn";
      case "ptAirportBus":
        return "Flughafenbus";
      case "ptTrainCAT":
        return "Flughafenzug";
      case "ptBusCity":
        return "Bus";
      case "ptBusNight":
        return "Nacht-Buss";
      case "ptBusRegion":
        return "Regional-Buss";
      case "ptTaxi":
        return "Taxi";
      case "walk":
        return "zu Fuß";
      default:
        return vehicleType;
    }
  }

  public static Optional<String> getDiva(String locationInfo) {
    try {
      JsonNode data = MAPPER.readTree(locationInfo).findValue("data");
      JsonNode pois = data.findValue("pois");
      return Optional.of(pois.get(0).findValue("properties").findValue("name").asText());
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Optional.empty();
    }
  }

}
