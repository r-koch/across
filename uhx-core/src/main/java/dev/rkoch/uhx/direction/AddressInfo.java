package dev.rkoch.uhx.direction;

import java.util.Objects;

public class AddressInfo {

  private final String address;

  private final String municipality;

  private final String longitude;

  private final String latitude;

  public static AddressInfo of(final String address, final String municipality,
      final String longitude, final String latitude) {
    return new AddressInfo(address, municipality, longitude, latitude);
  }

  private AddressInfo(final String address, final String municipality, final String longitude,
      final String latitude) {
    this.address = address;
    this.municipality = municipality;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public String getAddress() {
    return address;
  }

  public String getMunicipality() {
    return municipality;
  }

  public String getLongitude() {
    return longitude;
  }

  public String getLatitude() {
    return latitude;
  }

  @Override
  public int hashCode() {
    return Objects.hash(address, latitude, longitude, municipality);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AddressInfo other = (AddressInfo) obj;
    return Objects.equals(address, other.address) && Objects.equals(latitude, other.latitude)
        && Objects.equals(longitude, other.longitude)
        && Objects.equals(municipality, other.municipality);
  }

  @Override
  public String toString() {
    return "AddressInfo [address=" + address + ", municipality=" + municipality + ", longitude="
        + longitude + ", latitude=" + latitude + "]";
  }

}
