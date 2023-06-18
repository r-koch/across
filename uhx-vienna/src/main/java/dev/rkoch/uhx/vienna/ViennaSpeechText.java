package dev.rkoch.uhx.vienna;

import dev.rkoch.uhx.speech.SpeechText;

public final class ViennaSpeechText implements SpeechText {

  private static final String CANCEL_AND_STOP_TEXT = "Ende!";

  private static final String DEBUG_ACTIVE_TEXT = "Test Ausgabe an.";

  private static final String DEBUG_INACTIVE_TEXT = "Test Ausgabe aus.";

  private static final String ERROR_TEXT = "Programmfehler. Breche ab.";

  private static final String FALLBACK_TEXT = "Hab dich nicht verstandn.";

  private static final String HELP_TEXT =
      "Sag zum Beispiel: von Stephansplatz 12 nach Schönbrunner Schloßstraße 47.";

  private static final String LAUNCH_TEXT = "Ja?";

  @Override
  public String getCancelAndStop() {
    return CANCEL_AND_STOP_TEXT;
  }

  @Override
  public String getDebugActive() {
    return DEBUG_ACTIVE_TEXT;
  }

  @Override
  public String getDebugInactive() {
    return DEBUG_INACTIVE_TEXT;
  }

  @Override
  public String getError() {
    return ERROR_TEXT;
  }

  @Override
  public String getFallback() {
    return FALLBACK_TEXT;
  }

  @Override
  public String getHelp() {
    return HELP_TEXT;
  }

  @Override
  public String getLaunch() {
    return LAUNCH_TEXT;
  }

}
