package com.github.c5ms.cargotracker.interfaces.restful.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

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