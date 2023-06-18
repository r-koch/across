package dev.rkoch.uhx.handler;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import dev.rkoch.uhx.Context;
import dev.rkoch.uhx.shared.model.BuiltInIntent;
import dev.rkoch.uhx.util.HandlerInputHelper;

public class FallbackIntentHandler implements IntentRequestHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(FallbackIntentHandler.class);

  @Override
  public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
    return HandlerInputHelper.matches(input, BuiltInIntent.FALLBACK);
  }

  @Override
  public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
    try {
      if (HandlerInputHelper.isDebugMode(input)) {
        LOGGER.trace(intentRequest.toString());
      }

      return Context.getFallback();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Context.getError();
    }
  }

}
