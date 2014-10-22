package org.vaadin.alump.lazylayouts.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.VPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds lazy scroll notifying to client side implementation of Vaadin Panel
 */
public class LazyPanel extends VPanel implements LazyScrollNotifier {

    private final List<LazyScrollListener> scrollListeners = new ArrayList<LazyScrollListener>();

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONSCROLL) {
            for(LazyScrollListener listener : scrollListeners) {
                listener.onLazyScroll(Element.as(contentNode));
            }
        }
    }

    @Override
    public void addLazyScrollListener(LazyScrollListener listener) {
        scrollListeners.add(listener);
    }

    @Override
    public void removeLazyScrollListener(LazyScrollListener listener) {
        scrollListeners.remove(listener);
    }

    @Override
    public Element getLazyScrollingElement() {
        return Element.as(contentNode);
    }
}
