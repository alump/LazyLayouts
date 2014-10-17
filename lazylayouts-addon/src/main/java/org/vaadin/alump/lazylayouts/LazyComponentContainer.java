package org.vaadin.alump.lazylayouts;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Interface implemented by lazy loading component containers
 */
public interface LazyComponentContainer extends ComponentContainer {

    /**
     * Check if lazy loading is currently active in this lazy component container
     * @return true if lazy loading is enabled, false if not
     */
    boolean isLazyLoading();

    /**
     * Define component shown in container when lazy loading is enabled
     * @param component
     */
    void setLazyLoadingIndicator(Component component);

    /**
     * Get current loading indicator component used in container
     * @return Current loading indicator
     */
    Component getLazyLoadingIndicator();

    /**
     * Enable lazy loading with given component provider
     * @param provider Provider asked for new components based on client changes
     */
    void enableLazyLoading(LazyComponentProvider provider);

    /**
     * Disable lazy loading and return layout back to normal state.
     */
    void disableLazyLoading();

    /**
     * Get current component provider of container
     * @return Current component provider or null if not defined
     */
    LazyComponentProvider getLazyComponentProvider();

}
