package dev.rkoch.uhx.direction;

import java.util.Optional;

public class Address {

  private final Optional<String> houseNumber;

  private final Optional<String> streetName;

  private Address(Optional<String> streetName, Optional<String> houseNumber) {
    this.houseNumber = houseNumber;
    this.streetName = streetName;
  }

  public static Address of(final Optional<String> streetName, final Optional<String> houseNumber) {
    return new Address(streetName, houseNumber);
  }

  public Optional<String> getHouseNumber() {
    return houseNumber;
  }

  public Optional<String> getStreetName() {
    return streetName;
  }

}
