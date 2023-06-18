package dev.rkoch.uhx.vienna.manager.model;

import java.util.List;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.Intent;
import dev.rkoch.uhx.shared.model.CustomIntent;

public enum Debug {

  INSTANCE;

  public static Intent getIntent() {
    return INSTANCE.intent;
  }

  private final Intent intent;

  private Debug() {
    intent = Intent.builder() //
        .withName(CustomIntent.DEBUG) //
        .withSamples(List.of("test")) //
        .build();
  }

}
