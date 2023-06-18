package dev.rkoch.uhx.vienna.manager.model;

import java.util.List;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.Intent;
import dev.rkoch.uhx.shared.model.CustomIntent;

public enum ContactInformation {

  INSTANCE;

  public static Intent getIntent() {
    return INSTANCE.intent;
  }

  private final Intent intent;

  private ContactInformation() {
    intent = Intent.builder() //
        .withName(CustomIntent.CONTACT_INFORMATION) //
        .withSamples(List.of("kontakt")) //
        .build();
  }

}
