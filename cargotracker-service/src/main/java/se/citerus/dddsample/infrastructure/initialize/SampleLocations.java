package se.citerus.dddsample.infrastructure.initialize;

import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.UnLocode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sample locations, for test purposes.
 * 
 */
public class SampleLocations {

  public static final Location HONGKONG = new Location(UnLocode.of("CNHKG"), "Hongkong");
  public static final Location MELBOURNE = new Location(UnLocode.of("AUMEL"), "Melbourne");
  public static final Location STOCKHOLM = new Location(UnLocode.of("SESTO"), "Stockholm");
  public static final Location HELSINKI = new Location(UnLocode.of("FIHEL"), "Helsinki");
  public static final Location CHICAGO = new Location(UnLocode.of("USCHI"), "Chicago");
  public static final Location TOKYO = new Location(UnLocode.of("JNTKO"), "Tokyo");
  public static final Location HAMBURG = new Location(UnLocode.of("DEHAM"), "Hamburg");
  public static final Location SHANGHAI = new Location(UnLocode.of("CNSHA"), "Shanghai");
  public static final Location ROTTERDAM = new Location(UnLocode.of("NLRTM"), "Rotterdam");
  public static final Location GOTHENBURG = new Location(UnLocode.of("SEGOT"), "Göteborg");
  public static final Location HANGZHOU = new Location(UnLocode.of("CNHGH"), "Hangzhou");
  public static final Location NEWYORK = new Location(UnLocode.of("USNYC"), "New York");
  public static final Location DALLAS = new Location(UnLocode.of("USDAL"), "Dallas");

  public static final Map<UnLocode, Location> ALL = new HashMap<>();

  static {
    for (Field field : SampleLocations.class.getDeclaredFields()) {
      if (field.getType().equals(Location.class)) {
        try {
          Location location = (Location) field.get(null);
          ALL.put(UnLocode.of(location.getUnlocode()), location);
        } catch (IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public static List<Location> getAll() {
    return new ArrayList<>(ALL.values());
  }

  public static Location lookup(UnLocode unLocode) {
    return ALL.get(unLocode);
  }

}
