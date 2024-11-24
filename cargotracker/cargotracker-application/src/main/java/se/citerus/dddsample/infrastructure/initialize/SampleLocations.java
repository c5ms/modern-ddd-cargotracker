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
 */
public class SampleLocations {

    public static final Location HONGKONG =Location.of(UnLocode.of("CNHKG"), "Hongkong");
    public static final Location MELBOURNE =Location.of(UnLocode.of("AUMEL"), "Melbourne");
    public static final Location STOCKHOLM =Location.of(UnLocode.of("SESTO"), "Stockholm");
    public static final Location HELSINKI =Location.of(UnLocode.of("FIHEL"), "Helsinki");
    public static final Location CHICAGO =Location.of(UnLocode.of("USCHI"), "Chicago");
    public static final Location TOKYO =Location.of(UnLocode.of("JNTKO"), "Tokyo");
    public static final Location HAMBURG =Location.of(UnLocode.of("DEHAM"), "Hamburg");
    public static final Location SHANGHAI =Location.of(UnLocode.of("CNSHA"), "Shanghai");
    public static final Location ROTTERDAM =Location.of(UnLocode.of("NLRTM"), "Rotterdam");
    public static final Location GOTHENBURG =Location.of(UnLocode.of("SEGOT"), "Göteborg");
    public static final Location HANGZHOU =Location.of(UnLocode.of("CNHGH"), "Hangzhou");
    public static final Location NEWYORK =Location.of(UnLocode.of("USNYC"), "New York");
    public static final Location DALLAS =Location.of(UnLocode.of("USDAL"), "Dallas");

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
