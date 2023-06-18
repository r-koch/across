package dev.rkoch.uhx.handler;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.LaunchRequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import dev.rkoch.uhx.Context;

public class LaunchRequestHandlerImpl implements LaunchRequestHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(LaunchRequestHandlerImpl.class);

  @Override
  public boolean canHandle(HandlerInput input, LaunchRequest launchRequest) {
    return true;
  }

  @Override
  public Optional<Response> handle(HandlerInput input, LaunchRequest launchRequest) {
    try {
      return Context.getLaunch();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Context.getError();
    }
  }

}
