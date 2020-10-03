package com.taskagile.doman.common.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * The default implementation of DomainEventPublisher that
 * bases on Spring Application Event
 */
@Component
public class DefaultDomainEventPublisher implements DomainEventPublisher {

    @Autowired
    private ApplicationEventPublisher actualPublisher;

    @Override
    public void publish(DomainEvent event) {
        actualPublisher.publishEvent(event);
    }

}
