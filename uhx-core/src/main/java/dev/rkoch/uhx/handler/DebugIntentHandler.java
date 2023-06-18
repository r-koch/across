package dev.rkoch.uhx.handler;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import dev.rkoch.uhx.Context;
import dev.rkoch.uhx.shared.model.CustomIntent;
import dev.rkoch.uhx.util.HandlerInputHelper;

public class DebugIntentHandler implements IntentRequestHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(DebugIntentHandler.class);

  @Override
  public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
    return HandlerInputHelper.matches(input, CustomIntent.DEBUG);
  }

  @Override
  public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
    try {
      if (HandlerInputHelper.isDebugMode(input)) {
        LOGGER.trace(intentRequest.toString());
      }

      if (HandlerInputHelper.toggleDebugMode(input)) {
        return Context.getDebugActive();
      } else {
        return Context.getDebugInactive();
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Context.getError();
    }
  }

}
