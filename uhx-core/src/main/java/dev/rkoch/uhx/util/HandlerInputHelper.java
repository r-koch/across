package dev.rkoch.uhx.util;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.slu.entityresolution.Resolution;
import com.amazon.ask.model.slu.entityresolution.Resolutions;
import com.amazon.ask.model.slu.entityresolution.StatusCode;
import com.amazon.ask.model.slu.entityresolution.ValueWrapper;
import com.amazon.ask.request.Predicates;
import com.amazon.ask.request.RequestHelper;
import dev.rkoch.uhx.SessionAttribute;

public final class HandlerInputHelper {

	private HandlerInputHelper() {

	}

	public static String getMatchedSlotValue(final HandlerInput input, final String slotName) {
		Optional<Slot> slot = RequestHelper.forHandlerInput(input).getSlot(slotName);
		if (slot.isPresent()) {
			Resolutions resolutions = slot.get().getResolutions();
			if (resolutions != null) {
				for (Resolution resolution : resolutions.getResolutionsPerAuthority()) {
					List<ValueWrapper> values = resolution.getValues();
					if (!values.isEmpty()
							&& resolution.getStatus().getCode().compareTo(StatusCode.ER_SUCCESS_MATCH) == 0) {
						return values.get(0).getValue().getName();
					}
				}
			}
		}
		return null;
	}

	public static boolean matches(final HandlerInput input, final String intentName) {
		return input.matches(Predicates.intentName(intentName));
	}

	public static boolean toggleDebugMode(final HandlerInput input) {
		Boolean isDebug = getSessionAttributeValue(input, SessionAttribute.DEBUG, Boolean.class);
		if (isDebug != null) {
			putSessionAttribute(input, SessionAttribute.DEBUG, !isDebug);
			return !isDebug;
		} else {
			putSessionAttribute(input, SessionAttribute.DEBUG, Boolean.TRUE);
			return true;
		}
	}

	public static boolean isDebugMode(final HandlerInput input) {
		Boolean isDebug = getSessionAttributeValue(input, SessionAttribute.DEBUG, Boolean.class);
		if (isDebug != null) {
			return isDebug;
		} else {
			return false;
		}
	}

	public static void putSessionAttribute(final HandlerInput input, final String key, final Object value) {
		input.getAttributesManager().getSessionAttributes().put(key, value);
	}

	public static <T> T getSessionAttributeValue(final HandlerInput input, final String key, final Class<T> valueType) {
		Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
		Object value = sessionAttributes.get(key);
		if (valueType.isInstance(value)) {
			return valueType.cast(value);
		} else {
			return null;
		}
	}

}
