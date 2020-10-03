package com.taskagile.doman.common.event;

import org.springframework.context.ApplicationEvent;

public abstract class DomainEvent extends ApplicationEvent {

    private static final long serialVersionUID = -444783093811334147L;

    public DomainEvent(Object source) {
        super(source);
    }

    /**
     * Get the timestamp this event occurred
     * @return
     */
    public long occurredAt() {
        return getTimestamp();
    }
}
