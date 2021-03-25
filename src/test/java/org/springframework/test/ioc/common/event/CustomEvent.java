package org.springframework.test.ioc.common.event;

import org.springframework.context.event.ApplicationContextEvent;

public class CustomEvent extends ApplicationContextEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public CustomEvent(Object source) {
        super(source);
    }
}
