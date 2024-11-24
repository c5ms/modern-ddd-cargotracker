package se.citerus.dddsample.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.citerus.dddsample.application.command.HandlingReportReceiveCommand;
import se.citerus.dddsample.application.service.HandlingEventReceiver;

@Slf4j
@RequiredArgsConstructor
public class QueuedHandlingEventReceiver implements HandlingEventReceiver {

    @Override
    public void receiveHandlingReport(HandlingReportReceiveCommand command) {
        // todo to send command to message queue
    }

}
