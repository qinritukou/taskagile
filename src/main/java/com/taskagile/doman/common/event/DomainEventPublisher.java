package com.taskagile.doman.common.event;

public interface DomainEventPublisher {
    /**
     * Publish a domain event
     * @param event
     */
    void publish(DomainEvent event);
}
