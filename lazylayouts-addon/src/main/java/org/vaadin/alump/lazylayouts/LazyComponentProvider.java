package org.vaadin.alump.lazylayouts;

/**
 * Interface to be implemented by application logic providing the lazy loaded components
 */
public interface LazyComponentProvider {

    /**
     * Called when new component is requested by client
     * @param event Event containing information required to perform actions
     */
    void onLazyComponentRequest(LazyComponentRequestEvent event);
}
