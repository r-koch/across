package dev.rkoch.uhx.vienna.direction;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rkoch.uhx.direction.AddressInfo;

public final class AddressServiceClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceClient.class);

  private static final HttpClient CLIENT = HttpClient.newHttpClient();

  private static final String URL =
      "https://data.wien.gv.at/daten/OGDAddressService.svc/GetAddressInfo?crs=EPSG:4326&Address=";

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private AddressServiceClient() {

  }

  public static Optional<AddressInfo> getAddress(final String streetName,
      final String streetNumber) {
    return getAddress(Optional.ofNullable(streetName), Optional.ofNullable(streetNumber));
  }

  public static Optional<AddressInfo> getAddress(final Optional<String> streetName,
      final Optional<String> houseNumber) {
    AddressInfo validAddress = null;
    if (streetName.isPresent() && houseNumber.isPresent()) {
      validAddress = getAddress(streetName.get() + " " + houseNumber.get());
    }
    if (validAddress != null && houseNumber.isPresent()
        && !validAddress.getAddress().contains(houseNumber.get())) {
      validAddress = getAddress(validAddress.getAddress() + " " + houseNumber.get());
    }
    if (validAddress == null && streetName.isPresent()) {
      validAddress = getAddress(streetName.get());
    }
    return Optional.ofNullable(validAddress);
  }

  private static AddressInfo getAddress(final String address) {
    Set<String> phoneticVariants = new LinkedHashSet<>();
    phoneticVariants.add(replaceWithAbreviations(address.toLowerCase()));
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "eu", "äu");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "f", "ph");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "ei", "ai");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "ey", "ay");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "ei", "ey");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "ai", "ay");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "au", "ao");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "oo", "u");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "st.", "sankt");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "dr.", "doktor");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "ing.", "ingenieur");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "klg", "kleingarten");
    PhoneticUtil.addPhoneticVariants(phoneticVariants, "sdl", "siedlung");
    Set<AddressInfo> allAddressInfos = new HashSet<>();
    for (String phoneticVariant : phoneticVariants) {
      List<AddressInfo> addressInfos = getAddressInfos(phoneticVariant);
      allAddressInfos.addAll(addressInfos);
    }
    return getPhoneticMatch(address, allAddressInfos);
  }

  private static String replaceWithAbreviations(final String address) {
    String replaced = address;
    replaced = replaced.replace("sankt ", "st. ");
    replaced = replaced.replace("doktor ", "dr. ");
    replaced = replaced.replace("ingenieur ", "ing. ");
    replaced = replaced.replace("kleingarten ", "klg ");
    replaced = replaced.replace("siedlung ", "sdl ");
    return replaced;
  }

  private static AddressInfo getPhoneticMatch(final String address,
      final Collection<AddressInfo> addressInfos) {
    Map<String, AddressInfo> temp = new HashMap<>();
    List<String> addresses = new ArrayList<>();
    for (AddressInfo addressInfo : addressInfos) {
      addresses.add(addressInfo.getAddress());
      temp.put(addressInfo.getAddress(), addressInfo);
    }
    String mostSimilar = PhoneticUtil.getPhoneticMatch(address, addresses);
    return temp.get(mostSimilar);
  }

  private static List<AddressInfo> getAddressInfos(final String address) {
    List<AddressInfo> addressInfos = new ArrayList<>();
    try {
      URI uri = URI.create(URL + URLEncoder.encode(address, StandardCharsets.UTF_8));
      HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
      String response = CLIENT.send(request, BodyHandlers.ofString()).body();
      JsonNode features = MAPPER.readTree(response).findValue("features");
      if (features != null) {
        for (JsonNode feature : features) {
          String adresse = feature.findValue("Adresse").asText();
          String municipality = feature.findValue("Municipality").asText();

          JsonNode coordinates = feature.findValue("coordinates");
          String longitude = coordinates.get(0).asText();
          String latitude = coordinates.get(1).asText();
          addressInfos.add(AddressInfo.of(adresse, municipality, longitude, latitude));
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
    return addressInfos;
  }

}
