package org.vaadin.alump.lazylayouts.client;

import com.google.gwt.user.client.Element;

/**
 * Client side extension interface to hook scrolling events to legacy Vaadin components (like Panel)
 */
public interface LazyScrollNotifier {

    /**
     * Add listener for lazy scrolling events
     * @param listener
     */
    void addLazyScrollListener(LazyScrollListener listener);

    /**
     * Remove lazy scrolling events listener
     * @param listener
     */
    void removeLazyScrollListener(LazyScrollListener listener);

    /**
     * Get scrolling element (used in calculations)
     */
    Element getLazyScrollingElement();
}
