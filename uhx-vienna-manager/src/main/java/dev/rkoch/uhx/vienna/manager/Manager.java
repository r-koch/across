package dev.rkoch.uhx.vienna.manager;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.ask.model.services.ServiceException;
import com.amazon.ask.model.services.skillManagement.SkillManagementService;
import com.amazon.ask.smapi.SmapiClients;
import com.amazon.ask.smapi.model.v1.skill.SkillSummary;
import com.amazon.ask.smapi.model.v1.skill.Manifest.SkillManifestEnvelope;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.Intent;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.InteractionModelData;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.LanguageModel;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.SlotType;
import com.amazon.ask.smapi.model.v1.vendorManagement.Vendor;
import dev.rkoch.uhx.shared.model.BuiltInIntent;
import dev.rkoch.uhx.shared.util.ResourceUtil;
import dev.rkoch.uhx.vienna.manager.model.ContactInformation;
import dev.rkoch.uhx.vienna.manager.model.Debug;
import dev.rkoch.uhx.vienna.manager.model.TransitStationToStation;
import dev.rkoch.uhx.vienna.manager.model.TransitStationToStreet;
import dev.rkoch.uhx.vienna.manager.model.TransitStreetToStation;
import dev.rkoch.uhx.vienna.manager.model.TransitStreetToStreet;
import dev.rkoch.uhx.vienna.manager.model.UhxVienna;

public class Manager {

  private static final Logger LOGGER = LoggerFactory.getLogger(Manager.class);

  private static final String SKILL_ID = "amzn1.ask.skill.a4256f6f-801f-4a76-a279-2a9c1e3e4fa1";

  private final SkillManagementService service;

  public Manager() {
    service = SmapiClients.createDefault(ResourceUtil.readString("client_id.cred"),
        ResourceUtil.readString("client_secret.cred"),
        ResourceUtil.readString("refresh_token.cred"));
  }

  private void getInteractionModel() {
    InteractionModelData interactionModelData =
        service.getInteractionModelV1(SKILL_ID, "development", "de-DE");
    LOGGER.debug(interactionModelData.toString());
    // HostedSkillPermission alexaHostedSkillUserPermissionsV1 =
    // service.getAlexaHostedSkillUserPermissionsV1(skillId, "alexa::profile:email:read");
    // LOGGER.debug(alexaHostedSkillUserPermissionsV1.toString());
    SkillManifestEnvelope skillManifestV1 = service.getSkillManifestV1(SKILL_ID, "development");
    LOGGER.debug(skillManifestV1.toString());
  }

  private void updateInteractionModel() {
    try {
      InteractionModelData interactionModelData =
          service.getInteractionModelV1(SKILL_ID, "development", "de-DE");
      LanguageModel languageModel = getLanguageModel(interactionModelData);

      updateIntents(languageModel);

      updateSlotTypes(languageModel);

      service.setInteractionModelV1(SKILL_ID, "development", "de-DE", interactionModelData, null);
    } catch (ServiceException e) {
      LOGGER.error(e.getBody().toString(), e);
    }
  }

  private LanguageModel getLanguageModel(final InteractionModelData interactionModelData) {
    return interactionModelData.getInteractionModel().getLanguageModel();
  }

  private void updateIntents(LanguageModel languageModel) {
    List<Intent> intents = languageModel.getIntents();
    intents.removeIf((intent) -> !intent.getName().startsWith("AMAZON"));
    for (Intent intent : intents) {
      if (BuiltInIntent.HELP.equalsIgnoreCase(intent.getName())) {
        List<String> samples = intent.getSamples();
        samples.clear();
        samples.addAll(UhxVienna.HELP_SAMPLES);
        break;
      }
    }

    intents.add(Debug.getIntent());
    intents.add(ContactInformation.getIntent());

    intents.add(TransitStationToStation.getIntent());
    intents.add(TransitStationToStreet.getIntent());
    intents.add(TransitStreetToStation.getIntent());
    intents.add(TransitStreetToStreet.getIntent());
  }

  private void updateSlotTypes(final LanguageModel languageModel) {
    List<SlotType> types = languageModel.getTypes();
    types.clear();
    types.add(UhxVienna.MODEL.from);
    types.add(UhxVienna.MODEL.stationName);
    types.add(UhxVienna.MODEL.streetName);
    types.add(UhxVienna.MODEL.to);
  }

  public static void main(String[] args) throws Throwable {
    Manager manager = new Manager();
    manager.updateInteractionModel();
    // manager.getInteractionModel();
  }

}
