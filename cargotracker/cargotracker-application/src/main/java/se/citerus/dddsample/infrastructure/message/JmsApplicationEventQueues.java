package se.citerus.dddsample.infrastructure.message;

public interface JmsApplicationEventQueues {

   String QUEUE_NAME_CARGO_HANDLED_QUEUE="CargoHandledQueue";
   String QUEUE_NAME_CARGO_ARRIVED_QUEUE="CargoArrivedQueue";
   String QUEUE_NAME_CARGO_MISDIRECTED_QUEUE="CargoMisdirectedQueue";

}
