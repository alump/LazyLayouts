package org.vaadin.alump.lazylayouts;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.alump.lazylayouts.client.LazyLayoutServerRpc;
import org.vaadin.alump.lazylayouts.client.LazyVerticalLayoutState;

/**
 * LazyVerticalLayout adds lazy loading of UI content to normal VerticalLayout. It not just for lazy server to browser
 * loading, but more to allow lazy database to server loading.
 *
 * LazyVerticalLayout will request more content when user scrolls to the end of layout or if layout does not have
 * enough content to fill the currently available space. Applications can tell when to stop asking for new content.
 * Layout also contains loading indicator component that is shown automatically to user while content is waited from
 * server.
 *
 * @author Sami Viitanen
 */
public class LazyVerticalLayout extends VerticalLayout implements LazyComponentContainer, LazyLayoutServerRpc {

    protected LazyComponentProvider lazyProvider;

    /**
     * Create new instance of lazy vertical layout
     */
    public LazyVerticalLayout() {
        this((String)null);
    }

    /**
     * Create new instance of lazy vertical layout
     * @param loadingMessage Loading message shown in default loading indicator
     */
    public LazyVerticalLayout(String loadingMessage) {
        super();
        // Undefined height
        setHeight(-1, Unit.PIXELS);
        // Use default loading indicator
        setLazyLoadingIndicator(new LazyLoadingIndicator(loadingMessage));

        registerRpc(this, LazyLayoutServerRpc.class);
    }

    /**
     * Create new instance of lazy vertical layout
     * @param components Components added to layout
     */
    public LazyVerticalLayout(Component... components) {
        this((String)null, components);
    }

    /**
     * Create new instance of lazy vertical layout
     * @param loadingMessage Loading message shown in default loading indicator
     * @param components Components added to layout
     */
    public LazyVerticalLayout(String loadingMessage, Component... components) {
        super(components);
        // Undefined height
        setHeight(-1, Unit.PIXELS);
        // Use default loading indicator
        setLazyLoadingIndicator(new LazyLoadingIndicator(loadingMessage));

        registerRpc(this, LazyLayoutServerRpc.class);
    }

    @Override
    public LazyComponentProvider getLazyComponentProvider() {
        return lazyProvider;
    }

    @Override
    protected LazyVerticalLayoutState getState() {
        return (LazyVerticalLayoutState)super.getState();
    }

    @Override
    public boolean isLazyLoading() {
        return getState().lazyLoading;
    }

    @Override
    public void setLazyLoadingIndicator(Component component) {
        if(getState().lazyLoadingIndicator == component) {
            return;
        } else if(getState().lazyLoadingIndicator != null) {
            removeComponent(getLazyLoadingIndicator());
        }

        if(component.getParent() != this) {
            super.addComponent(component);
            getState().lazyLoadingIndicator = component;
            component.setVisible(getState().lazyLoading);
        }
    }

    @Override
    public Component getLazyLoadingIndicator() {
        return (Component)getState().lazyLoadingIndicator;
    }

    @Override
    public void enableLazyLoading(LazyComponentProvider provider) {
        if(provider == null) {
            throw new IllegalArgumentException("Lazy loading provider can not be null");
        }
        lazyProvider = provider;
        getState().lazyLoading = (lazyProvider != null);
        getLazyLoadingIndicator().setVisible(true);
    }

    @Override
    public void disableLazyLoading() {
        lazyProvider = null;
        getState().lazyLoading = false;
        getLazyLoadingIndicator().setVisible(false);
    }

    @Override
    public void onLazyLoadRequest() {
        // Ignore request if lazy provider not defined
        if(getLazyComponentProvider() == null) {
            return;
        }

        LazyComponentRequestEvent event = new LazyComponentRequestEvent(this);
        getLazyComponentProvider().onLazyComponentRequest(event);
    }

    /**
     * Define scrolling parent (usually Panel). If parent is not defined, client side will try to resolve it.
     * @param parent Parent that handles scrolling
     */
    public void setScrollingParent(Component parent) {
        getState().scrollingParent = parent;
    }

    @Override
    public void addComponent(Component component) {
        if(component != getLazyLoadingIndicator()) {
            super.removeComponent(getLazyLoadingIndicator());
        }

        super.addComponent(component);

        if(component != getLazyLoadingIndicator()) {
            super.addComponent(getLazyLoadingIndicator());
        }
    }

    @Override
    public void addComponent(Component component, int index) {
        super.addComponent(component, index);

        if(getComponentIndex(getLazyLoadingIndicator()) + 1 < getComponentCount()) {
            removeComponent(getLazyLoadingIndicator());
            addComponent(getLazyLoadingIndicator());
        }
    }
}
