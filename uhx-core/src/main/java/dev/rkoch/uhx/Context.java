package dev.rkoch.uhx;

import java.util.Optional;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import dev.rkoch.uhx.direction.Address;
import dev.rkoch.uhx.direction.DirectionService;
import dev.rkoch.uhx.direction.TransitStation;
import dev.rkoch.uhx.speech.SpeechText;
import dev.rkoch.uhx.util.SpeechUtil;

public enum Context {

  INSTANCE;

  private DirectionService directionService;

  private SpeechText speechText;

  public static void setSpeechText(final SpeechText speechText) {
    INSTANCE.speechText = speechText;
  }

  public static void setDirectionService(final DirectionService directionService) {
    INSTANCE.directionService = directionService;
  }

  private static Optional<Response> getResponse(final String speechText) {
    return getResponse(speechText, false);
  }

  private static Optional<Response> getResponse(final String speechText, final boolean endSession) {
    return new ResponseBuilder().withSpeech(SpeechUtil.asVicki(speechText))
        .withShouldEndSession(endSession).build();
  }

  public static Optional<Response> getCancelAndStop() {
    return getResponse(INSTANCE.speechText.getCancelAndStop(), true);
  }

  public static Optional<Response> getDebugActive() {
    return getResponse(INSTANCE.speechText.getDebugActive());
  }

  public static Optional<Response> getDebugInactive() {
    return getResponse(INSTANCE.speechText.getDebugInactive());
  }

  public static Optional<Response> getError() {
    return getResponse(INSTANCE.speechText.getError(), true);
  }

  public static Optional<Response> getFallback() {
    return getResponse(INSTANCE.speechText.getFallback());
  }

  public static Optional<Response> getHelp() {
    return getResponse(INSTANCE.speechText.getHelp());
  }

  public static Optional<Response> getLaunch() {
    return getResponse(INSTANCE.speechText.getLaunch());
  }

  public static Optional<Response> getDirection(Address from, Address to) {
    return getResponse(INSTANCE.directionService.getDirection(from, to));
  }

  public static Optional<Response> getDirection(Address from, TransitStation to) {
    return getResponse(INSTANCE.directionService.getDirection(from, to));
  }

  public static Optional<Response> getDirection(TransitStation from, Address to) {
    return getResponse(INSTANCE.directionService.getDirection(from, to));
  }

  public static Optional<Response> getDirection(TransitStation from, TransitStation to) {
    return getResponse(INSTANCE.directionService.getDirection(from, to));
  }

}
