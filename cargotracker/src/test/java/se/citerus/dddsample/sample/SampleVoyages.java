package se.citerus.dddsample.sample;

import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.voyage.CarrierMovement;
import se.citerus.dddsample.domain.model.voyage.Schedule;
import se.citerus.dddsample.domain.model.voyage.Voyage;
import se.citerus.dddsample.domain.model.voyage.VoyageNumber;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

import static se.citerus.dddsample.sample.SampleLocations.*;


/**
 * Sample carrier movements, for test purposes.
 */
public class SampleVoyages {

    public static final Voyage CM001 = createVoyage("CM001", STOCKHOLM, HAMBURG);
    public static final Voyage CM002 = createVoyage("CM002", HAMBURG, HONGKONG);
    public static final Voyage CM003 = createVoyage("CM003", HONGKONG, NEWYORK);
    public static final Voyage CM004 = createVoyage("CM004", NEWYORK, CHICAGO);
    public static final Voyage CM005 = createVoyage("CM005", CHICAGO, HAMBURG);
    public static final Voyage CM006 = createVoyage("CM006", HAMBURG, HANGZHOU);

    private static Voyage createVoyage(String id, Location from, Location to) {
        var schedule = Schedule.of(List.of(CarrierMovement.of(from, to, Instant.now(), Instant.now())));
        var voyageNumber = VoyageNumber.of(id);
        return Voyage.of(voyageNumber, schedule);
    }

    public static Instant toDate(final String date, final String time) {
        try {
            return Instant.parse(date + "T" + time + ":00Z");
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }
}
