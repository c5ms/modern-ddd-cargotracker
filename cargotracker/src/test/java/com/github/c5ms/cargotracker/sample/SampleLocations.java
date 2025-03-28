package com.github.c5ms.cargotracker.sample;

import com.github.c5ms.cargotracker.domain.model.location.Location;
import com.github.c5ms.cargotracker.domain.model.location.UnLocode;

/**
 * Sample locations, for test purposes.
 */
public class SampleLocations {

	public static final Location HONGKONG = Location.of(UnLocode.of("CNHKG"), "Hongkong");
	public static final Location MELBOURNE = Location.of(UnLocode.of("AUMEL"), "Melbourne");
	public static final Location STOCKHOLM = Location.of(UnLocode.of("SESTO"), "Stockholm");
	public static final Location HELSINKI = Location.of(UnLocode.of("FIHEL"), "Helsinki");
	public static final Location CHICAGO = Location.of(UnLocode.of("USCHI"), "Chicago");
	public static final Location TOKYO = Location.of(UnLocode.of("JNTKO"), "Tokyo");
	public static final Location HAMBURG = Location.of(UnLocode.of("DEHAM"), "Hamburg");
	public static final Location SHANGHAI = Location.of(UnLocode.of("CNSHA"), "Shanghai");
	public static final Location ROTTERDAM = Location.of(UnLocode.of("NLRTM"), "Rotterdam");
	public static final Location GOTHENBURG = Location.of(UnLocode.of("SEGOT"), "Göteborg");
	public static final Location HANGZHOU = Location.of(UnLocode.of("CNHGH"), "Hangzhou");
	public static final Location NEWYORK = Location.of(UnLocode.of("USNYC"), "New York");
	public static final Location DALLAS = Location.of(UnLocode.of("USDAL"), "Dallas");

}
