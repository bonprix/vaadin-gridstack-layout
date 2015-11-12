package de.bonprix.gridstacklayout.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractLayoutConnector;
import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.Connect;

import de.bonprix.gridstacklayout.GridStackLayout;

/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 bonprix Handelsgesellschaft mbH 
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Connects the client-side widget with the server-side component.
 * 
 * @author Sebastian Funck
 */
@Connect(GridStackLayout.class)
public class GridStackLayoutConnector extends AbstractLayoutConnector implements GridStackListener {

    private final GridStackLayoutServerRpc clientToServerRpc;
    private final GridStackLayoutClientRpc serverToClientRpc;
    
    private final List<Connector> attachedChildren = new ArrayList<>();

    /**
     * Constructor.
     */
    public GridStackLayoutConnector() {
        // CLIENT -> SERVER 
        this.clientToServerRpc = RpcProxy.create(GridStackLayoutServerRpc.class, this);
        
        // SERVER -> CLIENT
        this.serverToClientRpc = new GridStackLayoutClientRpc() {
            @Override
            public void removeNativeWidget(final String id) {
                getWidget().removeWidget(id);
            }
        };
        registerRpc(GridStackLayoutClientRpc.class, this.serverToClientRpc);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                   AbstractLayoutConnector                  */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    @Override
    protected Widget createWidget() {
        return ((GridStackLayoutWidget) GWT.create(GridStackLayoutWidget.class)).setServerRpc(this);
    }

    @Override
    public GridStackLayoutWidget getWidget() {
        return (GridStackLayoutWidget) super.getWidget();
    }

    @Override
    public GridStackLayoutState getState() {
        return (GridStackLayoutState) super.getState();
    }

    @Override
    public void onStateChanged(final StateChangeEvent event) {
        super.onStateChanged(event);
        
        GridStackLayoutWidget widget = getWidget();
        
        widget.setReadOnly(getState().isReadOnly);
        widget.setStackedModeWidth(getState().stackedModeWidth);

        for (GridStackWidget gridWidget : getState().children) {
            widget.setWidgetDimension(gridWidget.getId(), gridWidget.getDimension());
            widget.setWidgetResizable(gridWidget.getId(), gridWidget.isResizable());
            widget.setWidgetDraggable(gridWidget.getId(), gridWidget.isDraggable());
        }
    }
    
    @Override
    public void updateCaption(final ComponentConnector connector) {
        // No caption updating supported
    }

    @Override
    public void onConnectorHierarchyChange(final ConnectorHierarchyChangeEvent event) {

        // Attach all new children
        for (final ComponentConnector connector : getChildComponents()) {

            if (this.attachedChildren.contains(connector)) {
                continue;
            }

            final GridStackWidget gridstackWidget = getState().getWidgetByConnector(connector);
            getWidget().addWidget(gridstackWidget, connector.getWidget());

            this.attachedChildren.add(connector);
        }
        
        // Remove no longer attached children
        for (final ComponentConnector oldConnector : event.getOldChildren()) {

            if (oldConnector.getParent() == this) {
                continue;
            }

            final Widget oldChildWidget = oldConnector.getWidget();
            if (oldChildWidget.isAttached()) {
                getWidget().remove(oldChildWidget);
                this.attachedChildren.remove(oldConnector);
            }
        }
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                     GridStackListener                      */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    @Override
    public void onWidgetAdded(final String widgetId) {
        this.clientToServerRpc.onWidgetAdded(widgetId);
    }

    @Override
    public void onWidgetRemoved(final String widgetId) {
        this.clientToServerRpc.onWidgetRemoved(widgetId);
    }

    @Override
    public void onWidgetResized(final String widgetId) {
        this.clientToServerRpc.onWidgetResized(widgetId, getWidgetsDimensionData());
    }

    @Override
    public void onWidgetDragged(final String widgetId) {
        this.clientToServerRpc.onWidgetDragged(widgetId, getWidgetsDimensionData());
    }

    @Override
    public void onHeightChanged(final int height) {
        this.clientToServerRpc.onHeightChanged(height);
    }
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                       Private Helper                       */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Returns a map with the widget id's mapping to the widget dimensions.
     * @return
     */
    private Map<String, GridStackWidgetDimension> getWidgetsDimensionData() {
        final Map<String, GridStackWidgetDimension> data = new HashMap<String, GridStackWidgetDimension>();

        for (final GridStackWidget w : getState().children) {
            data.put(w.getId(), getWidget().getPositions(w.getId()));
        }

        return data;
    }
    
}
