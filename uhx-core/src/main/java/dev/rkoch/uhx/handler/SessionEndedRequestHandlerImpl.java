package dev.rkoch.uhx.handler;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.SessionEndedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import dev.rkoch.uhx.Context;
import dev.rkoch.uhx.util.HandlerInputHelper;

public class SessionEndedRequestHandlerImpl implements SessionEndedRequestHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SessionEndedRequestHandlerImpl.class);

  @Override
  public boolean canHandle(HandlerInput input, SessionEndedRequest sessionEndedRequest) {
    return true;
  }

  @Override
  public Optional<Response> handle(HandlerInput input, SessionEndedRequest sessionEndedRequest) {
    try {
      if (HandlerInputHelper.isDebugMode(input)) {
        LOGGER.trace(sessionEndedRequest.toString());
      }

      return input.getResponseBuilder().build();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Context.getError();
    }
  }

}
