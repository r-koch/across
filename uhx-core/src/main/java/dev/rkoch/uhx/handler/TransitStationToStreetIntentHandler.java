package dev.rkoch.uhx.handler;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.RequestHelper;
import dev.rkoch.uhx.Context;
import dev.rkoch.uhx.direction.Address;
import dev.rkoch.uhx.direction.TransitStation;
import dev.rkoch.uhx.shared.model.CustomIntent;
import dev.rkoch.uhx.shared.model.CustomSlotName;
import dev.rkoch.uhx.util.HandlerInputHelper;

public class TransitStationToStreetIntentHandler implements IntentRequestHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(TransitStationToStreetIntentHandler.class);

  @Override
  public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
    return HandlerInputHelper.matches(input, CustomIntent.TRANSIT_STATION_TO_STREET);
  }

  @Override
  public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
    try {
      if (HandlerInputHelper.isDebugMode(input)) {
        LOGGER.debug(input.getRequestEnvelopeJson().toString());
      }

      RequestHelper requestHelper = RequestHelper.forHandlerInput(input);

      Optional<String> stationNameFrom =
          requestHelper.getSlotValue(CustomSlotName.STATION_NAME_FROM);
      Optional<String> streetNameTo = requestHelper.getSlotValue(CustomSlotName.STREET_NAME_TO);
      Optional<String> houseNumberTo = requestHelper.getSlotValue(CustomSlotName.HOUSE_NUMBER_TO);

      if (HandlerInputHelper.isDebugMode(input)) {
        LOGGER.debug(CustomSlotName.STATION_NAME_FROM + ": " + stationNameFrom.toString());
        LOGGER.debug(CustomSlotName.STREET_NAME_TO + ": " + streetNameTo.toString());
        LOGGER.debug(CustomSlotName.HOUSE_NUMBER_TO + ": " + houseNumberTo.toString());
      }

      return Context.getDirection(TransitStation.of(stationNameFrom),
          Address.of(streetNameTo, houseNumberTo));
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Context.getError();
    }
  }

}
