package dev.rkoch.uhx.shared.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public final class CsvUtil {

  public static final CSVFormat CSV_FORMAT_EXCEL_DE_WITH_HEADER =
      CSVFormat.EXCEL.builder().setDelimiter(';').setHeader().build();

  public static final CSVFormat CSV_FORMAT_EXCEL_EN_WITH_HEADER =
      CSVFormat.EXCEL.builder().setHeader().build();

  public static final CSVFormat CSV_FORMAT_EXCEL_DE_WITHOUT_HEADER =
      CSVFormat.EXCEL.builder().setDelimiter(';').setSkipHeaderRecord(true).build();

  private CsvUtil() {

  }

  public static List<CSVRecord> getCsvRecords(final byte[] data, final CSVFormat csvFormat)
      throws IOException {
    try (CSVParser parser = csvFormat.parse(new BufferedReader(
        new InputStreamReader(new ByteArrayInputStream(data), StandardCharsets.UTF_8)))) {
      return parser.getRecords();
    }
  }
}
