package org.vaadin.alump.lazylayouts.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * Created by alump on 16/10/14.
 */
public interface LazyLayoutServerRpc extends ServerRpc {

    public void onLazyLoadRequest();
}
