package dev.rkoch.uhx.vienna.direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.commons.text.similarity.LevenshteinDistance;

public final class PhoneticUtil {

  private static final LevenshteinDistance LEVENSHTEIN_DISTANCE =
      LevenshteinDistance.getDefaultInstance();

  private static final ColognePhonetic COLOGNE_PHONETIC = new ColognePhonetic();

  public static boolean isPhoneticMatch(final String left, final String right) {
    return COLOGNE_PHONETIC.isEncodeEqual(left, right);
  }

  public static Set<String> getPhoneticVariants(final String input, final String target,
      final String replacement) {
    Set<String> variants = new LinkedHashSet<>();
    addVariant(variants, input, target, replacement);
    addVariant(variants, input, replacement, target);
    return variants;
  }

  public static void addPhoneticVariants(final Set<String> variants, final String target,
      final String replacement) {
    Set<String> temp = new LinkedHashSet<>();
    for (String variant : variants) {
      addVariant(temp, variant, target, replacement);
      addVariant(temp, variant, replacement, target);
    }
    variants.addAll(temp);
  }

  private static void addVariant(final Set<String> variants, final String input,
      final String target, final String replacement) {
    String temp = input;
    while (temp.contains(target)) {
      temp = temp.replaceFirst(target, replacement);
      variants.add(temp);
    }
  }

  public static List<String> getPhoneticMatches(final String sample, final String... candidates) {
    return getPhoneticMatches(sample, Arrays.asList(candidates));
  }

  public static List<String> getPhoneticMatches(final String sample,
      final Collection<String> candidates) {
    List<String> phoneticMatches = new ArrayList<>();
    String sampleUpperCase = sample.toUpperCase(Locale.GERMAN);
    for (String candidate : candidates) {
      String candidateUpperCase = candidate.toUpperCase(Locale.GERMAN);
      if (COLOGNE_PHONETIC.isEncodeEqual(sampleUpperCase, candidateUpperCase)) {
        phoneticMatches.add(candidate);
      }
    }
    return phoneticMatches;
  }

  public static String getMostPhoneticSimilar(final String sample,
      final Collection<String> candidates) {
    String mostSimilar = null;
    String samplePhonetic = COLOGNE_PHONETIC.colognePhonetic(sample);
    int lowestDistanceValue = Integer.MAX_VALUE;
    for (String candidate : candidates) {
      String candidatePhonetic = COLOGNE_PHONETIC.colognePhonetic(candidate);
      int distanceValue = LEVENSHTEIN_DISTANCE.apply(samplePhonetic, candidatePhonetic);
      if (distanceValue < lowestDistanceValue) {
        lowestDistanceValue = distanceValue;
        mostSimilar = candidate;
      }
    }
    return mostSimilar;
  }

  public static String getPhoneticMatch(final String sample, final String... candidates) {
    return getPhoneticMatch(sample, Arrays.asList(candidates));
  }

  public static String getPhoneticMatch(final String sample, final Collection<String> candidates) {
    String exactPhoneticMatch = null;
    String sampleUpperCase = sample.toUpperCase(Locale.GERMAN);
    int lowestDistanceValue = Integer.MAX_VALUE;
    String closestPhoneticMatch = null;

    int lowestPhoneticDistanceValue = Integer.MAX_VALUE;
    for (String candidate : candidates) {
      String candidateUpperCase = candidate.toUpperCase(Locale.GERMAN);
      if (COLOGNE_PHONETIC.isEncodeEqual(sampleUpperCase, candidateUpperCase)) {
        int distanceValue = LEVENSHTEIN_DISTANCE.apply(sampleUpperCase, candidateUpperCase);
        if (distanceValue < lowestDistanceValue) {
          lowestDistanceValue = distanceValue;
          exactPhoneticMatch = candidate;
        }
      } else {
        String samplePhonetic = COLOGNE_PHONETIC.colognePhonetic(sampleUpperCase);
        String candidatePhonetic = COLOGNE_PHONETIC.colognePhonetic(candidateUpperCase);
        // if (candidatePhonetic != null && candidatePhonetic.contains(samplePhonetic)) {
        int distanceValue = LEVENSHTEIN_DISTANCE.apply(samplePhonetic, candidatePhonetic);
        if (distanceValue < lowestPhoneticDistanceValue) {
          lowestPhoneticDistanceValue = distanceValue;
          closestPhoneticMatch = candidate;
        }
        // }
      }
    }
    if (exactPhoneticMatch != null) {
      return exactPhoneticMatch;
    } else {
      return closestPhoneticMatch;
    }
  }

  public static String getMostExactSimilar(final String sample,
      final Collection<String> candidates) {
    List<String> exactMatches = new ArrayList<>();
    for (String candidate : candidates) {
      if (candidate.toLowerCase(Locale.GERMAN).contains(sample.toLowerCase(Locale.GERMAN))
          || sample.toLowerCase(Locale.GERMAN).contains(candidate.toLowerCase(Locale.GERMAN))) {
        exactMatches.add(candidate);
      }
    }
    if (exactMatches.isEmpty()) {
      return null;
    } else if (exactMatches.size() == 1) {
      return exactMatches.get(0);
    } else {
      int lowestDistanceValue = Integer.MAX_VALUE;
      String mostExactSimilar = null;
      for (String exactMatch : exactMatches) {
        int distanceValue = LEVENSHTEIN_DISTANCE.apply(sample, exactMatch);
        if (distanceValue < lowestDistanceValue) {
          lowestDistanceValue = distanceValue;
          mostExactSimilar = exactMatch;
        }
      }
      return mostExactSimilar;
    }

  }

  private PhoneticUtil() {

  }

  public static void main(String[] args) {
//    System.out.println(getPhoneticMatch("erlaerstraße 62", "Erlaaer Platz", "Erlaaer Schleife",
//        "Erlaaer Straße", "Erlachgasse", "Erlachplatz", "Erlafstraße"));
    System.out.println(getPhoneticMatch("breuhaus gasse", "Breuergasse", "Bräuhausgasse"));
  }

}
