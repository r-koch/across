package dev.rkoch.uhx.vienna.manager.model;

import java.util.ArrayList;
import java.util.List;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.TypeValue;
import com.amazon.ask.smapi.model.v1.skill.interactionModel.TypeValueObject;

public final class ModelUtil {

  public static List<TypeValue> toTypeValues(final String... values) {
    return toTypeValues(List.of(values));
  }

  public static List<TypeValue> toTypeValues(final List<String> values) {
    List<TypeValue> typeValues = new ArrayList<>();
    for (String value : values) {
      typeValues.add(
          TypeValue.builder().withName(TypeValueObject.builder().withValue(value).build()).build());
    }
    return typeValues;
  }

  public static String sampleOf(final String... slotNames) {
    StringBuilder sb = new StringBuilder();
    for (String slotName : slotNames) {
      sb.append("{");
      sb.append(slotName);
      sb.append("} ");
    }
    return sb.toString().strip();
  }

  private ModelUtil() {

  }

}
