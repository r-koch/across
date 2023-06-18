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

public class TransitStreetToStationIntentHandler implements IntentRequestHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(TransitStreetToStationIntentHandler.class);

  @Override
  public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
    return HandlerInputHelper.matches(input, CustomIntent.TRANSIT_STREET_TO_STATION);
  }

  @Override
  public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
    try {
      if (HandlerInputHelper.isDebugMode(input)) {
        LOGGER.debug(input.getRequestEnvelopeJson().toString());
      }

      RequestHelper requestHelper = RequestHelper.forHandlerInput(input);

      Optional<String> streetNameFrom = requestHelper.getSlotValue(CustomSlotName.STREET_NAME_FROM);
      Optional<String> houseNumberFrom =
          requestHelper.getSlotValue(CustomSlotName.HOUSE_NUMBER_FROM);
      Optional<String> stationNameTo = requestHelper.getSlotValue(CustomSlotName.STATION_NAME_TO);

      if (HandlerInputHelper.isDebugMode(input)) {
        LOGGER.debug(CustomSlotName.STREET_NAME_FROM + ": " + streetNameFrom.toString());
        LOGGER.debug(CustomSlotName.HOUSE_NUMBER_FROM + ": " + houseNumberFrom.toString());
        LOGGER.debug(CustomSlotName.STATION_NAME_TO + ": " + stationNameTo.toString());
      }
      
      return Context.getDirection(Address.of(streetNameFrom, houseNumberFrom),
          TransitStation.of(stationNameTo));
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Context.getError();
    }
  }

}
