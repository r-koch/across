package dev.rkoch.uhx.vienna.manager.loader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.rkoch.uhx.shared.util.CsvUtil;
import dev.rkoch.uhx.shared.util.LoaderUtil;
import dev.rkoch.uhx.shared.util.ViennaCleanerUtil;

public class TransitStationLoader {

  private static final Logger LOGGER = LoggerFactory.getLogger(TransitStationLoader.class);

  private static final String URL =
      "https://www.wienerlinien.at/ogd_realtime/doku/ogd/wienerlinien-ogd-haltestellen.csv";

  public static void writeCleanTransitStationCsv() {
    try {
      List<CSVRecord> csvRecords = CSVParser.parse(LoaderUtil.getUrl(URL), StandardCharsets.UTF_8,
          CsvUtil.CSV_FORMAT_EXCEL_DE_WITH_HEADER).getRecords();
      Path outputPath = LoaderUtil.getPathToResource("transit_stations.csv");
      try (CSVPrinter printer = new CSVPrinter(Files.newBufferedWriter(outputPath),
          CsvUtil.CSV_FORMAT_EXCEL_DE_WITHOUT_HEADER)) {
        for (CSVRecord csvRecord : csvRecords) {
          String diva = csvRecord.get(0);
          String name = csvRecord.get(1);
          String longitude = csvRecord.get(4);
          String latitude = csvRecord.get(5);
          printer.print(ViennaCleanerUtil.cleanStationName(name));
          printer.print(diva);
          printer.print(longitude);
          printer.print(latitude);
          printer.println();
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  public static void main(String[] args) {
    writeCleanTransitStationCsv();
  }

}
