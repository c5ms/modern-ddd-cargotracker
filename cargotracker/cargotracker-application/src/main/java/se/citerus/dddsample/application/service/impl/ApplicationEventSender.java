package se.citerus.dddsample.application.service.impl;

import se.citerus.dddsample.application.shared.ApplicationEvent;

public interface ApplicationEventSender {
    void send(ApplicationEvent event);
}
