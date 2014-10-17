package org.vaadin.alump.lazylayouts;

/**
 * Event created when client requires more components to show
 */
public class LazyComponentRequestEvent {

    private LazyComponentContainer container;

    /**
     * New event instance
     * @param container Container that is source of this event
     */
    public LazyComponentRequestEvent(LazyComponentContainer container) {
        this.container = container;
    }

    /**
     * Get LazyComponentContainer that created this event
     * @return
     */
    public LazyComponentContainer getComponentContainer() {
        return container;
    }
}
