package com.github.c5ms.cargotracker.domain.shared;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
public abstract class AbstractEntity {

    private transient final @Transient Map<Class<?>, DomainEvent> domainEvents = new HashMap<>();

    @DomainEvents
    public Collection<DomainEvent> events() {
        return domainEvents.values();
    }

    @AfterDomainEventPublication
    protected void clearEvents() {
        domainEvents.clear();
    }

    protected void addEvent(DomainEvent event) {
        this.domainEvents.put(event.getClass(), event);
    }

}
