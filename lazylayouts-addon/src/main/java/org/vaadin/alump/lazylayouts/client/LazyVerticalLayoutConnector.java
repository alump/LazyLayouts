package org.vaadin.alump.lazylayouts.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.PostLayoutListener;
import com.vaadin.client.ui.VWindow;
import com.vaadin.client.ui.layout.MayScrollChildren;
import com.vaadin.client.ui.orderedlayout.VerticalLayoutConnector;
import com.vaadin.shared.ui.Connect;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;

import java.util.logging.Logger;

/**
 * Connector for LazyVerticalLayout
 */
@Connect(LazyVerticalLayout.class)
public class LazyVerticalLayoutConnector extends VerticalLayoutConnector implements LazyScrollListener, PostLayoutListener {

    private final static Logger LOGGER = Logger.getLogger(LazyVerticalLayoutConnector.class.getName());

    protected ComponentConnector scrollerFollowed;
    protected Element scrollingElement;
    protected boolean waitingResponse = false;
    protected boolean mustRelayout = false;
    private HandlerRegistration handlerRegistration;

    /**
     * How long is waited after connector hierarchy change until scroller position is checked automatically
     */
    private final static int DELAYED_CHECK_AFTER_CHANGE_MS = 250;

    @Override
    public LazyVerticalLayoutState getState() {
        return (LazyVerticalLayoutState)super.getState();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void onUnregister() {

        //requestTimer.cancel();

        removeScrollingHandlers();

        super.onUnregister();
    }

    private void removeScrollingHandlers() {
        if(handlerRegistration != null) {
            handlerRegistration.removeHandler();
            handlerRegistration = null;
        }

        if(scrollerFollowed != null) {
            Widget widget = scrollerFollowed.getWidget();

            if(widget instanceof LazyScrollNotifier) {
                ((LazyScrollNotifier) scrollerFollowed.getWidget()).removeLazyScrollListener(this);
            }
            scrollerFollowed = null;
        }
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {

        if(getParent() != null) {
            if (scrollerFollowed == null) {
                scrollerFollowed = resolveScrollingParent();
                if (scrollerFollowed != null) {
                    attachScrollingEvents(scrollerFollowed);
                } else {
                    LOGGER.severe("Failed to resolve scrolling parent!");
                }
            }
        } else {
            removeScrollingHandlers();
        }

        waitingResponse = false;
        super.onConnectorHierarchyChange(event);
    }

    @Override
    public void postLayout() {
        if(getParent() != null && mustRelayout) {
            Widget indicator = getLazyLoadingIndicator();
            if(indicator != null) {
                indicator.getElement().getStyle().setOpacity(0.5);
            }

            // Verify that we do not need to continue loading after hierarchy change
            if(scrollingElement != null && getState().lazyLoading) {
                if (!waitingResponse) {
                    if (checkIfLazyRequestRequired(scrollingElement)) {
                        sendLazyLoadRequest();
                    }
                }
            }
            mustRelayout = false;
        }
    }

    @Override
    public void onStateChanged(StateChangeEvent event) {
        if (event.hasPropertyChanged("childData")) {
            mustRelayout = true;
        }
        super.onStateChanged(event);

        if(event.hasPropertyChanged("lazyLoading")) {
            waitingResponse = false;
        }
    }

    protected void attachScrollingEvents(ComponentConnector connector) {
        Widget widget = connector.getWidget();
        if(widget instanceof LazyScrollNotifier) {
            LazyScrollNotifier not = (LazyScrollNotifier) widget;
            scrollingElement = not.getLazyScrollingElement();
            not.addLazyScrollListener(this);
        } else if (widget instanceof VWindow) {
            final VWindow window = (VWindow)widget;
            scrollingElement = window.contentPanel.getElement();
            handlerRegistration = ((VWindow)widget).contentPanel.addScrollHandler(new ScrollHandler() {
                @Override
                public void onScroll(ScrollEvent event) {
                    onLazyScroll(window.contentPanel.getElement());
                }
            });
        } else {
            LOGGER.severe("Failed to connect to scrolling events of " + connector.getClass().getName());
        }
    }

    protected ComponentConnector resolveScrollingParent() {

        if(getState().scrollingParent != null) {
            return (ComponentConnector)getState().scrollingParent;
        }

        ServerConnector connector = getParent();
        if(connector == null) {
            LOGGER.severe("LazyVerticalLayout not in connector hierarchy.");
            return null;
        }

        while(connector != null) {
            if(connector instanceof MayScrollChildren) {
                return (ComponentConnector)connector;
            }
            connector = connector.getParent();
        }

        LOGGER.severe("Failed to resolve scrolling parent for LazyVerticalLayout");
        return null;
    }

    @Override
    public void onLazyScroll(Element scrollingElement) {

        // Can be ignored, if lazy loading not enabled
        if(!getState().lazyLoading) {
            return;
        }

        // Remember element so it can be used to check current position
        this.scrollingElement = scrollingElement;

        // Send load request if no pending request and position requires new content
        if(!waitingResponse && checkIfLazyRequestRequired(scrollingElement)) {
            sendLazyLoadRequest();
        }
    }

    /**
     * Check if given scrolling element is at position that requires to perform lazy load
     * @param scrollingElement Element that is scrollable
     * @return true if lazy loading is required, false if not
     */
    protected boolean checkIfLazyRequestRequired(Element scrollingElement) {
        if(scrollingElement == null) {
            LOGGER.severe("Can not check scroll position of undefined element");
            return false;
        }

        Widget indicator = getLazyLoadingIndicator();
        if(indicator != null) {
            int indicatorY = indicator.getElement().getAbsoluteTop() - scrollingElement.getAbsoluteTop();
            return (indicatorY < scrollingElement.getClientHeight());
        } else {
            int maxScroll = scrollingElement.getScrollHeight() - scrollingElement.getClientHeight();
            return (scrollingElement.getScrollTop() >= maxScroll);
        }
    }

    /**
     * Sends lazy component loading request to server
     */
    protected void sendLazyLoadRequest() {
        if(!waitingResponse && LazyVerticalLayoutConnector.this.isEnabled()) {
            waitingResponse = true;
            LOGGER.fine("Sending lazy loading request...");
            Widget indicator = getLazyLoadingIndicator();
            if (indicator != null) {
                indicator.getElement().getStyle().setOpacity(1.0);
            }
            getRpcProxy(LazyLayoutServerRpc.class).onLazyLoadRequest();
        }
    }

    /**
     * Timer used to delay request until scrolling stops
     */
    /*
    protected class LazyRequestTimer extends Timer {

        public final static int REQUEST_DELAY_TIMER_MS = 50;

        public LazyRequestTimer() {

        }

        public void schedule() {
            schedule(REQUEST_DELAY_TIMER_MS);
        }

        @Override
        public void run() {
            if(LazyVerticalLayoutConnector.this.isEnabled()) {
                LOGGER.fine("Sending lazy loading request...");
                Widget indicator = getLazyLoadingIndicator();
                if (indicator != null) {
                    indicator.getElement().getStyle().setOpacity(1.0);
                }
                getRpcProxy(LazyLayoutServerRpc.class).onLazyLoadRequest();
            }
        }
    };

    protected LazyRequestTimer requestTimer = new LazyRequestTimer();
*/
    /**
     * Gets lazy loading indicator
     * @return Loading indicator widget instance if defined
     */
    protected Widget getLazyLoadingIndicator() {
        ComponentConnector connector = (ComponentConnector)getState().lazyLoadingIndicator;
        if(connector == null) {
            return null;
        }
        return connector.getWidget();
    }
}
