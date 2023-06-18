package dev.rkoch.uhx.handler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.ServiceClientFactory;
import com.amazon.ask.model.services.ups.UpsServiceClient;
import dev.rkoch.uhx.Context;
import dev.rkoch.uhx.shared.model.CustomIntent;
import dev.rkoch.uhx.util.HandlerInputHelper;
import dev.rkoch.uhx.util.SpeechUtil;

public class ContactInformationIntentHandler implements IntentRequestHandler {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ContactInformationIntentHandler.class);

  @Override
  public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {
    return HandlerInputHelper.matches(input, CustomIntent.CONTACT_INFORMATION);
  }

  @Override
  public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {
    try {
      if (HandlerInputHelper.isDebugMode(input)) {
        LOGGER.trace(intentRequest.toString());
      }

      String apiAccessToken =
          input.getRequestEnvelope().getContext().getSystem().getApiAccessToken();
      logEmail(apiAccessToken);
      // LOGGER.error(input.getRequestEnvelope().getContext().getSystem().getUser().getPermissions()
      // .toString());

      StringBuilder speechText = new StringBuilder();

      // ServiceClientFactory serviceClientFactory = input.getServiceClientFactory();
      // LOGGER.debug(input.getRequestEnvelope().getContext().getSystem().toString());

      // LOGGER.debug(input.getRequestEnvelope().getContext().getSystem().getUser().getPermissions()
      // .toString());
      //
      // String deviceId =
      // input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
      // LOGGER.debug(deviceId);
      //
      ServiceClientFactory serviceClientFactory = input.getServiceClientFactory();

      // serviceClientFactory.getDeviceAddressService().
      // DeviceAddressServiceClient deviceAddressService =
      // serviceClientFactory.getDeviceAddressService();
      // Address fullAddress = deviceAddressService.getFullAddress(deviceId);
      // LOGGER.debug(fullAddress.toString());

      // UpsServiceClient upsService = serviceClientFactory.getUpsService();
      // LOGGER.debug(upsService.getProfileEmail());

      // DefaultApiConfiguration defaultApiConfiguration =
      // DefaultApiConfiguration.builder().build();
      // ServiceClientFactory serviceClientFactory =
      // ServiceClientFactory.builder().withDefaultApiConfiguration(defaultApiConfiguration).build();
      // serviceClientFactory.

      UpsServiceClient upsService = serviceClientFactory.getUpsService();
      try {
        String profileEmail = upsService.getProfileEmail();
        speechText.append("Email: ");
        speechText.append(profileEmail);
        speechText.append(". ");
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
      }
      // try {
      // String profileGivenName = upsService.getProfileGivenName();
      // speechText.append("Name: ");
      // speechText.append(profileGivenName);
      // speechText.append(". ");
      // } catch (Exception e) {
      // LOGGER.error(e.getMessage(), e);
      // }
      // try {
      // PhoneNumber profileMobileNumber = upsService.getProfileMobileNumber();
      // speechText.append("Telefonnummer: ");
      // speechText.append(profileMobileNumber.toString());
      // speechText.append(". ");
      // } catch (Exception e) {
      // LOGGER.error(e.getMessage(), e);
      // }
      //
      // try {
      // Address fullAddress = serviceClientFactory.getDeviceAddressService()
      // .getFullAddress(RequestHelper.forHandlerInput(input).getDeviceId());
      // speechText.append("Adresse: ");
      // speechText.append(fullAddress.toString());
      // speechText.append(". ");
      // } catch (Exception e) {
      // LOGGER.error(e.getMessage(), e);
      // }

      speechText.append("das wars.");

      // List<String> permissions = Arrays.asList("alexa::profile:email:read");

      return input.getResponseBuilder().withSpeech(SpeechUtil.asVicki(speechText.toString()))
          .withShouldEndSession(false).build();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      return Context.getError();
    }
  }

  private void logEmail(final String token) {
    LOGGER.error("token: " + token);
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI
            .create("https://api.eu.amazonalexa.com/v2/accounts/~current/settings/Profile.email"))
        .header("accept", "application/json").header("Authorization", "Bearer " + token).build();
    HttpResponse<String> response = null;
    try {
      response = client.send(request, BodyHandlers.ofString());
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
    LOGGER.error(response.body());
  }

}
