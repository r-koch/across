package dev.rkoch.uhx.util;

public final class SpeechUtil {

	private SpeechUtil() {

	}

	public static String asVicki(final String speechText) {
		return "<voice name='Vicki'>" + speechText + "</voice>";
	}
	
	public static String asVicki(final StringBuilder speechText) {
		return "<voice name='Vicki'>" + speechText.toString() + "</voice>";
	}

}
