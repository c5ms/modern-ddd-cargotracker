package com.github.c5ms.cargotracker.application.service.impl;

import com.github.c5ms.cargotracker.application.shared.ApplicationEvent;

/**
 * To send application integration events to outside infrastructure
 * <p>
 * e.g. event -> messageQueue; event -> localFile
 */
public interface ApplicationEventMessageSender {
    void send(ApplicationEvent event);
}
