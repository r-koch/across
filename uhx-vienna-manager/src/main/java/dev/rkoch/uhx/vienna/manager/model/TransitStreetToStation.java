package dev.rkoch.uhx.vienna.manager.model;

import java.util.List;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.Intent;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.SlotDefinition;
import dev.rkoch.uhx.shared.model.BuiltInSlotType;
import dev.rkoch.uhx.shared.model.CustomIntent;
import dev.rkoch.uhx.shared.model.CustomSlotName;

public enum TransitStreetToStation {

  INSTANCE;

  public static Intent getIntent() {
    return INSTANCE.intent;
  }

  private final Intent intent;

  private TransitStreetToStation() {
    intent = Intent.builder() //
        .withName(CustomIntent.TRANSIT_STREET_TO_STATION) //
        .withSamples(getSamples()) //
        .withSlots(getSlotDefinitions()) //
        .build();
  }

  private List<String> getSamples() {
    return List.of(ModelUtil.sampleOf(CustomSlotName.FROM, CustomSlotName.STREET_NAME_FROM,
        CustomSlotName.HOUSE_NUMBER_FROM, CustomSlotName.TO, CustomSlotName.STATION_NAME_TO));
  }

  private List<SlotDefinition> getSlotDefinitions() {
    return List.of(
        SlotDefinition.builder().withName(CustomSlotName.FROM).withType(UhxVienna.SLOT_TYPE_FROM)
            .build(),
        SlotDefinition.builder().withName(CustomSlotName.STREET_NAME_FROM)
            .withType(UhxVienna.SLOT_TYPE_STREET_NAME).build(),
        SlotDefinition.builder().withName(CustomSlotName.HOUSE_NUMBER_FROM)
            .withType(BuiltInSlotType.NUMBER).build(),
        SlotDefinition.builder().withName(CustomSlotName.STATION_NAME_TO)
            .withType(UhxVienna.SLOT_TYPE_STATION_NAME).build(),
        SlotDefinition.builder().withName(CustomSlotName.TO).withType(UhxVienna.SLOT_TYPE_TO)
            .build());
  }

}
