package se.citerus.dddsample.application.service.impl;

import se.citerus.dddsample.application.shared.ApplicationEvent;

public interface ApplicationEventMessageSender {
    void send(ApplicationEvent event);
}
