package org.vaadin.alump.lazylayouts.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.VPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alump on 16/10/14.
 */
public class LazyPanel extends VPanel implements LazyScrollNotifier {

    private final List<LazyScrollListener> scrollListeners = new ArrayList<LazyScrollListener>();

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONSCROLL) {
            for(LazyScrollListener listener : scrollListeners) {
                listener.onLazyScroll(contentNode);
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
        return contentNode;
    }
}
