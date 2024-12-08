package se.citerus.dddsample.interfaces.restful.support;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringToTrackingIdConvertorTest {

    StringToTrackingIdConvertor convertor = new StringToTrackingIdConvertor();

    @Test
    void convert() {
        var trackingId = convertor.convert("0001");
        assertNotNull(trackingId);
        assertEquals("0001", trackingId.getId());
    }

    @Test
    void convert_empty_throw_exception() {
        assertThrows(IllegalArgumentException.class, () -> convertor.convert(""));
    }


}