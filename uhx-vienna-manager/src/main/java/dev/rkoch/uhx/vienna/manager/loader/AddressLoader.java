package dev.rkoch.uhx.vienna.manager.loader;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.rkoch.uhx.shared.util.CsvUtil;
import dev.rkoch.uhx.shared.util.LoaderUtil;
import dev.rkoch.uhx.shared.util.ViennaCleanerUtil;

public final class AddressLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddressLoader.class);

  private static final String URL =
      "https://data.wien.gv.at/daten/geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:ADRESSENOGD&srsName=EPSG:4326&outputFormat=csv&propertyname=NAME_STR,NAME_ONR&cql_filter=GEB_BEZIRK=";

  private AddressLoader() {

  }

  public static void getAddresses() {
    Set<String> streetNames = new HashSet<>();
    for (int i = 1; i <= 23; i++) {
      String bezirk = String.format("'%02d'", i);
      LOGGER.debug("bezirk: " + bezirk);
      try {
        List<CSVRecord> csvRecords = CSVParser.parse(LoaderUtil.getUrl(URL + bezirk),
            StandardCharsets.UTF_8, CsvUtil.CSV_FORMAT_EXCEL_EN_WITH_HEADER).getRecords();
        for (CSVRecord csvRecord : csvRecords) {
          String streetname = csvRecord.get(2).toLowerCase(Locale.GERMAN);
          if (streetname.contains("sonderadresse")) {
            continue;
          }
          streetNames.add(ViennaCleanerUtil.cleanAddress(streetname));
        }
        Path path = LoaderUtil.getPathToResource("street_names.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
          for (String streetName : streetNames) {
            writer.append(streetName);
            writer.newLine();
          }
        }
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
      }
    }
  }

  public static void main(String[] args) throws Throwable {
    getAddresses();
  }

}
