package dev.rkoch.uhx.direction;

import java.util.Optional;

public class TransitStation {

  private final Optional<String> name;

  private TransitStation(final Optional<String> name) {
    this.name = name;
  }

  public static TransitStation of(final Optional<String> name) {
    return new TransitStation(name);
  }

  public Optional<String> getName() {
    return name;
  }

}
