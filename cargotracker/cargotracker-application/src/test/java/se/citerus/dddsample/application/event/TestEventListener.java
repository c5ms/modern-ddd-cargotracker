package se.citerus.dddsample.application.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import se.citerus.dddsample.application.shared.ApplicationEvent;
import se.citerus.dddsample.domain.shared.DomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class TestEventListener {

    private final List<DomainEvent> domainEvents = new ArrayList<>();
    private final List<ApplicationEvent> applicationEvents = new ArrayList<>();

    @EventListener(classes = DomainEvent.class)
    public void on(DomainEvent event) {
        domainEvents.add(event);

    }

    @EventListener(classes = ApplicationEvent.class)
    public void on(ApplicationEvent event) {
        applicationEvents.add(event);
    }

    public boolean hasApplicationEvent(Predicate<ApplicationEvent> predicate) {
        return applicationEvents.stream()
            .anyMatch(predicate);
    }

    public boolean hasDomainEvent(Predicate<DomainEvent> predicate) {
        return domainEvents.stream()
            .anyMatch(predicate);
    }

}
