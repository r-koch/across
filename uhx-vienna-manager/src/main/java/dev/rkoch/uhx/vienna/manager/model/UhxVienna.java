package dev.rkoch.uhx.vienna.manager.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVRecord;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.SlotType;
import dev.rkoch.uhx.shared.util.CsvUtil;

public enum UhxVienna {

  MODEL;

  public static final String SLOT_TYPE_FROM = "FROM";
  public static final String SLOT_TYPE_STATION_NAME = "STATION_NAME";
  public static final String SLOT_TYPE_STREET_NAME = "STREET_NAME";
  public static final String SLOT_TYPE_TO = "TO";

  public static final List<String> HELP_SAMPLES = List.of("anleitung", //
      "erklärung", //
      "erklär mir", //
      "hilfe", //
      "wie geht", //
      "wie funktioniert", //
      "was", //
      "was kann");

  public final SlotType from;

  public final SlotType stationName;

  public final SlotType streetName;

  public final SlotType to;

  private List<String> getStationNameValues() {
    try {
      Path path = Paths.get(Thread.currentThread().getContextClassLoader()
          .getResource("transit_stations.csv").toURI());
      List<CSVRecord> csvRecords =
          CsvUtil.getCsvRecords(Files.readAllBytes(path), CsvUtil.CSV_FORMAT_EXCEL_DE_WITH_HEADER);
      List<String> stationNames = new ArrayList<>();
      for (CSVRecord csvRecord : csvRecords) {
        stationNames.add(csvRecord.get(0));
      }
      return stationNames;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> getStreetNameValues() {
    try {
      Path path = Paths.get(
          Thread.currentThread().getContextClassLoader().getResource("street_names.txt").toURI());
      return Files.readAllLines(path);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private List<String> getFromValues() {
    return List.of("von", "ab", "start", "abfahrt"/* , "wie komme ich von", "wie komm ich von" */);
  }

  private List<String> getToValues() {
    return List.of("nach", "bis", "zu", "ziel", "ankunft");
  }

  private UhxVienna() {
    from = SlotType.builder() //
        .withName(SLOT_TYPE_FROM) //
        .withValues(ModelUtil.toTypeValues(getFromValues())) //
        .build();
    stationName = SlotType.builder() //
        .withName(SLOT_TYPE_STATION_NAME) //
        .withValues(ModelUtil.toTypeValues(getStationNameValues())) //
        .build();
    streetName = SlotType.builder() //
        .withName(SLOT_TYPE_STREET_NAME) //
        .withValues(ModelUtil.toTypeValues(getStreetNameValues())) //
        .build();
    to = SlotType.builder() //
        .withName(SLOT_TYPE_TO) //
        .withValues(ModelUtil.toTypeValues(getToValues())) //
        .build();
  }

}
