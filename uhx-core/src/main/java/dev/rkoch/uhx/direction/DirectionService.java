package dev.rkoch.uhx.direction;

public interface DirectionService {

  public String getDirection(Address from, Address to);

  public String getDirection(Address from, TransitStation to);

  public String getDirection(TransitStation from, Address to);

  public String getDirection(TransitStation from, TransitStation to);

}
