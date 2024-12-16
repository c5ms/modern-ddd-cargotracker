package se.citerus.dddsample.application.service.impl;

import se.citerus.dddsample.application.shared.ApplicationEvent;

/**
 * To send application integration events to outside infrastructure
 * <p>
 * e.g. event -> messageQueue; event -> localFile
 */
public interface ApplicationEventMessageSender {
    void send(ApplicationEvent event);
}
