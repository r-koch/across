package dev.rkoch.uhx.vienna;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import dev.rkoch.uhx.Context;
import dev.rkoch.uhx.handler.CancelAndStopIntentHandler;
import dev.rkoch.uhx.handler.ContactInformationIntentHandler;
import dev.rkoch.uhx.handler.DebugIntentHandler;
import dev.rkoch.uhx.handler.FallbackIntentHandler;
import dev.rkoch.uhx.handler.HelpIntentHandler;
import dev.rkoch.uhx.handler.LaunchRequestHandlerImpl;
import dev.rkoch.uhx.handler.SessionEndedRequestHandlerImpl;
import dev.rkoch.uhx.handler.TransitStationToStationIntentHandler;
import dev.rkoch.uhx.handler.TransitStationToStreetIntentHandler;
import dev.rkoch.uhx.handler.TransitStreetToStationIntentHandler;
import dev.rkoch.uhx.handler.TransitStreetToStreetIntentHandler;
import dev.rkoch.uhx.vienna.direction.ViennaDirectionService;

public class ViennaStreamHandler extends SkillStreamHandler {

  private static final String SKILL_ID = "amzn1.ask.skill.a4256f6f-801f-4a76-a279-2a9c1e3e4fa1";

  private static Skill getSkill() {
    return Skills.standard() //
        .withSkillId(SKILL_ID) //
        .addRequestHandler(new DebugIntentHandler()) //
        .addRequestHandler(new ContactInformationIntentHandler()) //

        .addRequestHandler(new TransitStationToStationIntentHandler()) //
        .addRequestHandler(new TransitStationToStreetIntentHandler()) //
        .addRequestHandler(new TransitStreetToStationIntentHandler()) //
        .addRequestHandler(new TransitStreetToStreetIntentHandler()) //

        .addRequestHandler(new CancelAndStopIntentHandler()) //
        .addRequestHandler(new FallbackIntentHandler()) //
        .addRequestHandler(new HelpIntentHandler()) //
        .addRequestHandler(new LaunchRequestHandlerImpl()) //
        .addRequestHandler(new SessionEndedRequestHandlerImpl()) //
        .build();
  }

  public ViennaStreamHandler() {
    super(getSkill());
    Context.setDirectionService(new ViennaDirectionService());
    Context.setSpeechText(new ViennaSpeechText());
  }

}
