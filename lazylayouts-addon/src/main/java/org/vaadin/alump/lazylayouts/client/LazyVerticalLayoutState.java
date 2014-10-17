package org.vaadin.alump.lazylayouts.client;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.orderedlayout.VerticalLayoutState;

/**
 * State for LazyVerticalLayout
 */
public class LazyVerticalLayoutState extends VerticalLayoutState {
    /**
     * If lazy loading should be enabled
     */
    public boolean lazyLoading = false;

    /**
     * Lazy loading indicator component
     */
    public Connector lazyLoadingIndicator;

    /**
     * Scrolling parent (null if to be auto resolved)
     */
    public Connector scrollingParent;
}
