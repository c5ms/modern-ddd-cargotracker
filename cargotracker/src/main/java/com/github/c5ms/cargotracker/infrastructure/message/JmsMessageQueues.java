package com.github.c5ms.cargotracker.infrastructure.message;

public interface JmsMessageQueues {

    String QUEUE_NAME_CARGO_HANDLED_QUEUE = "CargoHandledQueue";
    String QUEUE_NAME_CARGO_ARRIVED_QUEUE = "CargoArrivedQueue";
    String QUEUE_NAME_CARGO_MISDIRECTED_QUEUE = "CargoMisdirectedQueue";
    String QUEUE_NAME_HANDLING_REPORT_QUEUE = "HandlingReportQueue";

}
