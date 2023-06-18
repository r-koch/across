package dev.rkoch.uhx.shared.util;

import java.util.Locale;

public final class ViennaCleanerUtil {

  private ViennaCleanerUtil() {}

  public static String cleanStationName(final String rawStationName) {
    String cleanedStationName = rawStationName.toLowerCase(Locale.GERMAN) //
        .replace("-", " ") //
        .replace("/", " ") //
        .replace("str.", "straße") //
        .replace("str ", "straße ") //
        .replace("st. ", "sankt ") //
        .replace("dr. ", "doktor ") //
        .replace("1.tor", "erstes tor") //
        .replace("2.tor", "zweites tor") //
        .replace("3.tor", "drittes tor") //
        .replace("4.tor", "viertes tor") //
        .replace("7.tor", "siebentes tor") //
        .replace("8.tor", "achtes tor") //
        .replace("7. haidequerstraße", "siebente haidequerstraße") //
        .replace("11. haidequerstraße", "elfte haidequerstraße") //
        .replace("12. februar platz", "zwölfter februar platz") //
        .replace("zentralfriedh ", "zentralfriedhof ") //
        .replace("friedh ", "friedhof ") //
        .replace("kriegsgr ", "kriegsgräber ") //
        .replace("halle3 ", "halle drei ") //
        .replace("gr ", "gruppe ") //
        .replace("1.wk", "erster weltkrieg") //
        .replace("h. von buol gasse", "heinrich von buol gasse") //
        .replace("btf. gürtel", "betriebshof gürtel") //
        .replace("veterinärmed. universität", "veterinärmedizinische universität"); //

    if (cleanedStationName.endsWith("str")) {
      cleanedStationName = cleanedStationName + "aße";
    }
    if (cleanedStationName.endsWith("pl")) {
      cleanedStationName = cleanedStationName + "atz";
    }

    return cleanedStationName;
  }

  public static String cleanAddress(final String streetname) {
    return streetname //
        .replace("-", " ") //
        .replace("u bahn station ", "") //
        .replace("st.", "sankt") //
        .replace("dr.", "doktor") //
        .replace("ing.", "ingenieur") //
        .replace("klg ", "kleingarten ") //
        .replace("sdl ", "siedlung ") //
        .replace("sp ", "") //
        .replace("(", "") //
        .replace(")", "") //
        .replace("wr.", "") //
        .replace("stad.", "stadion") //
        .replaceAll("\\.([^ ])", ". \\1") //
        .strip();
  }

}
