package dev.rkoch.uhx.vienna.direction;

import java.util.Optional;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.TransitDetails;
import dev.rkoch.uhx.direction.Address;
import dev.rkoch.uhx.direction.AddressInfo;
import dev.rkoch.uhx.direction.DirectionService;
import dev.rkoch.uhx.direction.GoogleDirectionService;
import dev.rkoch.uhx.direction.TransitStation;

public class ViennaDirectionService implements DirectionService {

  private final GoogleDirectionService googleGeoApi;

  public ViennaDirectionService() {
    googleGeoApi = new GoogleDirectionService("de", "at");
  }

  @Override
  public String getDirection(Address from, Address to) {
    Optional<AddressInfo> originInfo =
        AddressServiceClient.getAddress(from.getStreetName(), from.getHouseNumber());
    Optional<AddressInfo> destinationInfo =
        AddressServiceClient.getAddress(to.getStreetName(), to.getHouseNumber());

    if (originInfo.isPresent() && destinationInfo.isPresent()) {
      DirectionsResult directionsResult =
          googleGeoApi.getDirection(originInfo.get(), destinationInfo.get());
      String origin = from.getStreetName().get() + from.getHouseNumber().get();
      String destination = to.getStreetName().get() + to.getHouseNumber().get();
      return getDirectionDescription(origin, destination, directionsResult);
    } else {
      // TODO not found text
      return null;
    }

  }

  @Override
  public String getDirection(Address from, TransitStation to) {
    Optional<AddressInfo> originInfo =
        AddressServiceClient.getAddress(from.getStreetName(), from.getHouseNumber());
    Optional<AddressInfo> destinationInfo = TransitStationDirectory.getAddress(to.getName());

    if (originInfo.isPresent() && destinationInfo.isPresent()) {
      DirectionsResult directionsResult =
          googleGeoApi.getDirection(originInfo.get(), destinationInfo.get());
      String origin = from.getStreetName().get() + from.getHouseNumber().get();
      String destination = to.getName().get();
      return getDirectionDescription(origin, destination, directionsResult);
    } else {
      // TODO not found text
      return null;
    }
  }

  @Override
  public String getDirection(TransitStation from, Address to) {
    Optional<AddressInfo> originInfo = TransitStationDirectory.getAddress(from.getName());
    Optional<AddressInfo> destinationInfo =
        AddressServiceClient.getAddress(to.getStreetName(), to.getHouseNumber());

    if (originInfo.isPresent() && destinationInfo.isPresent()) {
      DirectionsResult directionsResult =
          googleGeoApi.getDirection(originInfo.get(), destinationInfo.get());
      String origin = from.getName().get();
      String destination = to.getStreetName().get() + to.getHouseNumber().get();
      return getDirectionDescription(origin, destination, directionsResult);
    } else {
      // TODO not found text
      return null;
    }
  }

  @Override
  public String getDirection(TransitStation from, TransitStation to) {
    Optional<String> direction =
        QuandoFpaServiceClient.getDirection(TransitStationDirectory.getDiva(from.getName()),
            TransitStationDirectory.getDiva(to.getName()));
    if (direction.isPresent()) {
      return direction.get();
    } else {
      return getNotFoundSpeechText(from.getName(), to.getName());
    }
  }

  private String getNotFoundSpeechText(final Optional<String> stationNameFrom,
      final Optional<String> stationNameTo) {
    StringBuilder speechText = new StringBuilder();
    speechText.append("Adresse nicht erkannt. ");
    appendNotFoundFragment(speechText, "Start-Station", stationNameFrom);
    appendNotFoundFragment(speechText, "Ziel-Station", stationNameTo);
    return speechText.toString();
  }

  private void appendNotFoundFragment(final StringBuilder speechText, final String label,
      final Optional<String> value) {
    if (value.isPresent()) {
      speechText.append(" ");
      speechText.append(label);
      speechText.append(" ");
      speechText.append(value.get());
    }
  }

  private static String getDirectionDescription(final String origin, final String destination,
      final DirectionsResult result) {
    StringBuilder builder = new StringBuilder();
    builder.append("Von ");
    builder.append(origin);
    builder.append(" nach ");
    builder.append(destination);
    builder.append(": ");
    for (DirectionsRoute directionsRoute : result.routes) {
      for (DirectionsLeg directionsLeg : directionsRoute.legs) {
        String lastStop = null;
        for (DirectionsStep directionsStep : directionsLeg.steps) {
          TransitDetails transitDetails = directionsStep.transitDetails;
          if (transitDetails != null) {
            String departureStopName = cleanStopName(transitDetails.departureStop.name);
            String arrivalStopName = cleanStopName(transitDetails.arrivalStop.name);
            String vehicleName = transitDetails.line.vehicle.name;
            String lineName = transitDetails.line.shortName;
            if (lineName == null) {
              lineName = transitDetails.line.name;
            }
            if (lastStop != null && lastStop.startsWith(departureStopName)) {
              builder.append("umsteigen zu ");
            } else {
              builder.append("mit ");
            }
            builder.append(vehicleName).append(' ');
            builder.append(lineName).append(' ');
            if (lastStop == null) {
              builder.append("von ");
              builder.append(departureStopName).append(' ');
            }
            builder.append("bis ");
            builder.append(arrivalStopName).append(", ");
            lastStop = arrivalStopName;
          }
        }
        if (builder.length() == 0) {
          builder.append("Ziel ");
          builder.append(cleanDuration(directionsLeg.duration.humanReadable));
          builder.append(" zu Fuß entfernt.");
        } else {
          builder.setLength(builder.length() - 2);
          builder.append(". Weegzeit ");
          builder.append(cleanDuration(directionsLeg.duration.humanReadable));
          builder.append('.');
        }
      }
    }
    return builder.toString().strip();
  }

  private static String cleanDuration(final String duration) {
    return duration.replace(", ", " und ");
  }

  private static String cleanStopName(final String stopName) {
    return stopName.replace('+', ' ').replace(" S ", " ").replaceAll(" U$", "")
        .replaceAll(" S$", "").replaceAll(" U([^a-zäöüß])", "$1");
  }

}
