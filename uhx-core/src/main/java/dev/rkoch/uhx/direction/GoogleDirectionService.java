package dev.rkoch.uhx.direction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TransitRoutingPreference;
import com.google.maps.model.TravelMode;
import dev.rkoch.uhx.shared.util.ResourceUtil;

public final class GoogleDirectionService implements AutoCloseable {

  private static final Logger LOGGER = LoggerFactory.getLogger(GoogleDirectionService.class);

  private final GeoApiContext context;

  private final String language;

  private final String region;

  public GoogleDirectionService(final String language, final String region) {
    context = new GeoApiContext.Builder().apiKey(getApiKey()).build();
    this.language = language;
    this.region = region;
  }

  private String getApiKey() {
    try {
      return ResourceUtil.readString("google_geo_api_key.cred");
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  public DirectionsResult getDirection(final AddressInfo originInfo,
      final AddressInfo destinationInfo) {
    String origin = originInfo.getLatitude() + "," + originInfo.getLongitude();
    String destination = destinationInfo.getLatitude() + "," + destinationInfo.getLongitude();
    return requestDirection(origin, destination);
  }

  public DirectionsResult getDirection(final String origin, final String destination) {
    return requestDirection(origin, destination);
  }

  private DirectionsResult requestDirection(final String origin, final String destination) {
    try {
      DirectionsApiRequest request = DirectionsApi.getDirections(context, origin, destination);
      request.language(language);
      request.mode(TravelMode.TRANSIT);
      request.region(region);
      request.transitRoutingPreference(TransitRoutingPreference.LESS_WALKING);
      return request.await();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws Exception {
    // double lng = 16.37155482032052;
    // double lat = 48.208272264890645;
    // LatLng latlng = new LatLng(lat, lng);
    // NearbySearchRequest nearbySearchQuery = PlacesApi.nearbySearchQuery(GEO_API_CONNTEXT,
    // latlng);
    // nearbySearchQuery.type(PlaceType.SUBWAY_STATION);
    // // nearbySearchQuery.type(PlaceType.BUS_STATION);
    // // nearbySearchQuery.type(PlaceType.TRAIN_STATION);
    // // nearbySearchQuery.type(PlaceType.LIGHT_RAIL_STATION);
    // nearbySearchQuery.radius(500);
    // PlacesSearchResponse placesSearchResponse = nearbySearchQuery.await();
    //
    // for (PlacesSearchResult result : Arrays.asList(placesSearchResponse.results)) {
    // System.out.println(result);
    // }
    // System.out.println(placesSearchResponse.results[0]);
    // System.out.println(placesSearchResponse);
    // String direction = getDirection("Wien, Lützowgasse 6", "Wien, Gasometer");
    // String direction = getDirection("Wien, Lützowgasse 6", "Wien, Sindelargasse 10");
    // String direction = getDirection("Wien, Stock im Weg", "Wien, Karlsplatz");
    // String direction = getDirection("Haltestelle Schrankenberggasse", "Haltestelle Achengasse");

    // System.out.println(getDirection("Lützowgasse 6", "Gasometer"));
    // System.out.println(getDirection("marc aurel straße 1", "passauer platz 1"));
    // System.out.println(getDirection("klg josef winter", "passauer platz 1"));
    // long time = 0;
    // getDirection("wien, klg josef winter", "wien, passauer platz 1");
    // time = System.currentTimeMillis();
    // for (int i = 0; i < 10; i++) {
    // getDirection("wien, hütteldorferstraße 211", "wien, ubahn station heiligenstadt");
    // }
    // System.out.println("duration: " + (System.currentTimeMillis() - time));
    // time = System.currentTimeMillis();
    // for (int i = 0; i < 10; i++) {
    // getDirection("48.19726006620652,16.29489001243074", "48.24922006622014,16.365190013123787");
    // }
    // System.out.println("duration: " + (System.currentTimeMillis() - time));

    // getDirection(
    // AddressInfo.of("hütteldorfer straße lützowgasse", null, "16.29489001243074",
    // "48.19726006620652"),
    // AddressInfo.of("stephansplatz", null, "16.37166001234869", "48.208070066040165"));
    // long time = System.currentTimeMillis();
    // // hütteldorfer straße lützowgasse;16.29489001243074;48.19726006620652
    // // stephansplatz;16.37166001234869;48.208070066040165
    // String direction = getDirection(
    // AddressInfo.of("hütteldorfer straße lützowgasse", null, "16.29489001243074",
    // "48.19726006620652"),
    // AddressInfo.of("stephansplatz", null, "16.37166001234869", "48.208070066040165"));
    // System.out.println(direction);
    // System.out.println("duration: " + (System.currentTimeMillis() - time));

  }

  @Override
  public void close() throws Exception {
    context.close();
  }

}
