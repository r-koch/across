package dev.rkoch.uhx.vienna.direction;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.rkoch.uhx.direction.AddressInfo;
import dev.rkoch.uhx.shared.util.CsvUtil;
import dev.rkoch.uhx.shared.util.ViennaCleanerUtil;

public enum TransitStationDirectory {

  INSTANCE;

  private static final Logger LOGGER = LoggerFactory.getLogger(TransitStationDirectory.class);

  private static final String URL =
      "https://www.wienerlinien.at/ogd_realtime/doku/ogd/wienerlinien-ogd-haltestellen.csv";

  private static final Map<String, AddressInfo> ADDRESS_DIR = new HashMap<>();

  private static final Map<String, String> DIVA_DIR = new HashMap<>();

  private TransitStationDirectory() {

  }

  static {
    try (CSVParser parser =
        CSVParser.parse(getUrl(), StandardCharsets.UTF_8, CsvUtil.CSV_FORMAT_EXCEL_DE_WITH_HEADER)) {
      for (CSVRecord csvRecord : parser) {
        String diva = csvRecord.get(0);
        String stationName = ViennaCleanerUtil.cleanStationName(csvRecord.get(1));
        String longitude = csvRecord.get(4);
        String latitude = csvRecord.get(5);
        DIVA_DIR.put(stationName, diva);
        ADDRESS_DIR.put(stationName, AddressInfo.of(stationName, null, longitude, latitude));
      }
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  private static URL getUrl() {
    try {
      return new URL(URL);
    } catch (MalformedURLException e) {
      LOGGER.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  public static Optional<AddressInfo> getAddress(final String stationName) {
    return getAddress(Optional.ofNullable(stationName));
  }

  public static Optional<AddressInfo> getAddress(final Optional<String> stationName) {
    if (stationName.isPresent()) {
      return Optional.ofNullable(ADDRESS_DIR.get(stationName.get().toLowerCase(Locale.GERMAN)));
    } else {
      return Optional.empty();
    }
  }

  public static Optional<String> getDiva(final String stationName) {
    return getDiva(Optional.ofNullable(stationName));
  }

  public static Optional<String> getDiva(final Optional<String> stationName) {
    if (stationName.isPresent()) {
      return Optional.ofNullable(
          DIVA_DIR.get(PhoneticUtil.getPhoneticMatch(stationName.get(), DIVA_DIR.keySet())));
    } else {
      return Optional.empty();
    }
  }

  public static void main(String[] args) {
    System.out.println(TransitStationDirectory.getDiva("karlsplatz"));
    // System.out.println(TransitStationDirectory.getDiva("lützowgasse"));
    // System.out.println(TransitStationDirectory.getDiva("oberlaa"));
    // System.out.println(TransitStationDirectory.getDiva("heiligenstadt"));
    // System.out.println(TransitStationDirectory.getDiva("stock im weg"));
    // System.out.println(TransitStationDirectory.getDiva("gerasdorf postamt"));
    // System.out.println(TransitStationDirectory.getDiva("hütteldorfer straße lützowgasse"));
    // System.out.println(
    // PhoneticUtil.getPhoneticMatch("hütteldorfer straße lützowgasse", DIVA_DIR.keySet()));
    System.out.println(TransitStationDirectory.getAddress("karlsplatz"));
  }

}
