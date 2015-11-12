package de.bonprix.gridstacklayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;

import de.bonprix.gridstacklayout.GridWidgetDragListener.GridWidgetDragEvent;
import de.bonprix.gridstacklayout.GridWidgetResizeListener.GridWidgetResizeEvent;
import de.bonprix.gridstacklayout.client.GridStackLayoutClientRpc;
import de.bonprix.gridstacklayout.client.GridStackLayoutServerRpc;
import de.bonprix.gridstacklayout.client.GridStackLayoutState;
import de.bonprix.gridstacklayout.client.GridStackWidget;
import de.bonprix.gridstacklayout.client.GridStackWidgetDimension;

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
 * A vaadin layout that arranged the added components in draggable and resizable widgets inside a grid.
 * 
 * @author Sebastian Funck
 *
 */
@JavaScript({ "jquery-1.11.3.min.js", "jquery-ui-1.11.2.min.js", "lodash-3.5.0.min.js", "gridstack-0.2.3.js" })
public class GridStackLayout extends AbstractLayout {

    private static final long serialVersionUID = -7888248805522465765L;

    /**
     * Grid modes a grid layout can be in.
     * 
     * @author Sebastian Funck
     */
    public enum GridMode {
        NORMAL,
        STACKED
    }

    private final Map<Component, String> componentToId = new HashMap<Component, String>();
    private final Map<String, Component> idToComponent = new HashMap<String, Component>();
    
    private final Map<String, GridStackWidget> idToGridstackWidget = new HashMap<String, GridStackWidget>();

    private final GridStackLayoutClientRpc serverToClientRpc;
    private final GridStackLayoutServerRpc clientToServerRpc;
    
    /**
     * Public-Constructor.
     */
    public GridStackLayout() {
        setWidth("100%");

        // SERVER -> CLIENT
        this.serverToClientRpc = getRpcProxy(GridStackLayoutClientRpc.class);

        // CLIENT -> SERVER
        this.clientToServerRpc = new GridStackLayoutServerRpc() {

            @Override
            public void onWidgetAdded(final String widgetId) {
            	fireWidgetAdded(widgetId);
            }

            @Override
            public void onWidgetRemoved(final String widgetId) {
                fireWidgetRemoved(widgetId);
            }

            @Override
            public void onWidgetDragged(final String srcWidgetId, final Map<String, GridStackWidgetDimension> allWidgetDimensions) {
            	fireDragged(srcWidgetId, allWidgetDimensions);
            }

            @Override
            public void onWidgetResized(final String srcWidgetId, final Map<String, GridStackWidgetDimension> allWidgetDimensions) {
            	fireResized(srcWidgetId, allWidgetDimensions);
            }

            @Override
            public void onHeightChanged(final int pixels) {
                setHeight(pixels, Unit.PIXELS);
            }

        };
        registerRpc(this.clientToServerRpc);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                         Features                           */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * If the grid widget that contains <i>component</i> is resizable.
     * 
     * @param component The component
     * @return If the widget that contains <i>component</i> is resizable.
     * 
     * @throws IllegalArgumentException if <i>component</i> is not attached to this layout
     */
    public boolean isWidgetResizable(final Component component) {
        return getState().getWidgetByConnector(component).isResizable();
    }

    /**
     * Set the resizability of a grid widget that contains <i>component</i>.
     * 
     * @param component The component
     * @param resizable If the grid widget is resizable
     * 
     * @throws IllegalArgumentException if <i>component</i> is not attached to this layout
     */
    public void setWidgetResizable(final Component component, final boolean resizable) {
        getState().getWidgetByConnector(component).setResizable(resizable);
    }

    /**
     * If the grid widget that contains <i>component</i> is draggable.
     * 
     * @param component The component
     * @return If the widget that contains <i>component</i> is draggable.
     * 
     * @throws IllegalArgumentException if <i>component</i> is not attached to this layout
     */
    public boolean isWidgetDraggable(final Component component) {
        return getState().getWidgetByConnector(component).isDraggable();
    }

    /**
     * Set the draggability of a grid widget that contains <i>component</i>.
     * 
     * @param component The component
     * @param draggable If the grid widget is draggable
     * 
     * @throws IllegalArgumentException if <i>component</i> is not attached to this layout
     */
    public void setWidgetDraggable(final Component component, final boolean draggable) {
        getState().getWidgetByConnector(component).setDraggable(draggable);
    }

    /**
     * The current grid mode.
     * 
     * @return The current grid mode
     */
    public GridMode getGridMode() {
        return getState().isStackedMode ? GridMode.STACKED : GridMode.NORMAL;
    }

    /**
     * Returns the grid positions and dimensions of the grid widget that wraps <i>component</i>.
     * 
     * @param component The component
     * @return The grid positions and dimensions of the grid widget that wraps <i>component</i>
     * 
     * @throws IllegalArgumentException if <i>component</i> is not attached to this layout
     */
    public GridStackWidgetDimension getComponentDimension(final Component component) {
        return getState().getWidgetByConnector(component).getDimension();
    }

    /**
     * Sets the dimension of the widget that wraps <i>component</i>.
     *
     * @param component The component
     * @param dimension The dimension for the wrapping widget
     * 
     * @throws IllegalArgumentException if <i>component</i> is not attached to this layout
     */
    public void setComponentDimension(final Component component, final GridStackWidgetDimension dimension) {
        getState().getWidgetByConnector(component).setDimension(dimension);
    }

    /**
     * The width when the grid enters the <i>GridMode.STACKED</i> mode.
     * 
     * @param widthInPixels The width in pixels
     */
    public void setStackedModeWidth(final int widthInPixels) {
        getState(true).stackedModeWidth = widthInPixels;
    }
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                         Listener                           */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Adds a widget drag listener.
     * 
     * @param dragListener The widget drag listener
     */
    public void addGridWidgetDragListener(final GridWidgetDragListener dragListener) {
        addListener(GridWidgetDragEvent.class, dragListener, GridWidgetDragListener.componentDragMethod);
    }

    /**
     * Removes a widget drag listener.
     * 
     * @param dragListener The widget drag listener
     */
    public void removeGridWidgetDragListener(final GridWidgetDragListener dragListener) {
        removeListener(GridWidgetDragEvent.class, dragListener, GridWidgetDragListener.componentDragMethod);
    }
    
    /**
     * Adds a widget resize listener.
     * 
     * @param resizeListener The widget resize listener
     */
    public void addWidgetResizeListener(final GridWidgetResizeListener resizeListener) {
        addListener(GridWidgetResizeEvent.class, resizeListener, GridWidgetResizeListener.componentResizeMethod);
    }

    /**
     * Removes a widget resize listener.
     * 
     * @param resizeListener The widget resize listener
     */
    public void removeWidgetResizeListener(final GridWidgetResizeListener resizeListener) {
        removeListener(GridWidgetResizeEvent.class, resizeListener, GridWidgetResizeListener.componentResizeMethod);
    }
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                          Vaadin                            */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    @Override
    public GridStackLayoutState getState() {
        return (GridStackLayoutState) super.getState();
    }

    @Override
    public GridStackLayoutState getState(final boolean markAsDirty) {
        return (GridStackLayoutState) super.getState(markAsDirty);
    }

    @Override
    public void addComponent(final Component c) {
        addComponent(c, 0, 0, 1, 1);
    }
    
    public void addComponent(final Component c, final int x, final int y, final int width, final int height) {
        addComponent(c, new GridStackWidgetDimension(x, y, width, height));
    }
    
    public void addComponent(final Component component, final GridStackWidgetDimension dimension) {
        if (this.componentToId.containsKey(component)) {
            throw new IllegalArgumentException("Component is already attached to this layout");
        }

        final String id = UUID.randomUUID().toString();

        final GridStackWidget widget = new GridStackWidget(id, component, dimension);
        this.idToGridstackWidget.put(id, widget);
        
        linkComponentToWidgetId(id, component);

        getState().addGridStackWidget(widget);
        super.addComponent(component);

        setComponentDimension(component, dimension);
    }

    @Override
    public void removeComponent(final Component c) {
        removeComponent(c, true);
    }

    @Override
    public void removeAllComponents() {
        final LinkedList<Component> tempAllChildren = new LinkedList<Component>();

        // Adds all components to temporary list
        for (final Component component : this) {
            tempAllChildren.add(component);
        }

        // Removes all component
        for (final Component component : tempAllChildren) {
            removeComponent(component, false);
        }
    }

    @Override
    // sfunck TODO: Implement this
    public void replaceComponent(final Component oldComponent, final Component newComponent) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        getState(true).readOnly = readOnly;
    }

    @Override
    public int getComponentCount() {
        return this.idToGridstackWidget.size();
    }

    @Override
    public Iterator<Component> iterator() {
        return this.componentToId.keySet()
                                 .iterator();
    }

    // PRIVATE HELPER METHODS
    private void removeComponent(final Component component, final boolean fireEvent) {
        if (!this.componentToId.containsKey(component)) {
            return;
        }

        final String widgetId = this.componentToId.get(component);

        this.idToGridstackWidget.remove(widgetId);
        
        unlinkComponentToWidget(widgetId, component);

        
        getState().children.remove(component);
        super.removeComponent(component);

        this.serverToClientRpc.removeNativeWidget(widgetId);
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                          Events                            */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    private void fireWidgetAdded(final String id) {
    	// TODO: fire widget added event
    }
    
    private void fireWidgetRemoved(final String id) {
    	// TODO: fire widget removed event
    }
    
    private void fireDragged(final String widgetId, final Map<String, GridStackWidgetDimension> updatedWidgetDimensions) {
    	onGridUpdate(updatedWidgetDimensions);

    	// Fire event
    	Component component = getComponentByWidgetId(widgetId);
        GridStackWidget widget = getGridStackWidgetById(widgetId);

        fireEvent(new GridWidgetDragEvent(this, component, widget.getDimension()));
    }
    
    private void fireResized(final String widgetId, final Map<String, GridStackWidgetDimension> updatedWidgetDimensions) {
    	// Update shared state
    	onGridUpdate(updatedWidgetDimensions);

    	// Fire event
    	Component component = getComponentByWidgetId(widgetId);
        GridStackWidget widget = getGridStackWidgetById(widgetId);

        fireEvent(new GridWidgetResizeEvent(this, component, widget.getDimension()));
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                  Widget.id <-> Component                   */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    private boolean linkExists(final String widgetId, final Component component) {
    	Component tmpComponent = this.idToComponent.get(widgetId);
    	String tmpId = this.componentToId.get(component);
    	
    	return  component.equals(tmpComponent) && widgetId.equals(tmpId);
    }
    
    private void linkComponentToWidgetId(final String widgetId, final Component component) {
    	if (this.idToComponent.containsKey(widgetId) || this.componentToId.containsKey(component)) {
    		return;
    	}
    	
        this.componentToId.put(component, widgetId);
        this.idToComponent.put(widgetId, component);
    }
    
    private void unlinkComponentToWidget(final String widgetId, final Component component) {
    	if (!this.idToComponent.containsKey(widgetId) || !this.componentToId.containsKey(component)) {
    		return;
    	}
    	
        this.componentToId.remove(component);
        this.idToComponent.remove(widgetId);
    }
    
    private Component getComponentByWidgetId(final String widgetId) {
    	return this.idToComponent.get(widgetId);
    }
    
    private String getWidgetIdByComponent(final Component component) {
    	return this.componentToId.get(component);
    }
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                       Helper Methods                       */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    

    private GridStackWidget getGridStackWidgetById(final String widgetId) {
    	return this.idToGridstackWidget.get(widgetId);
    }
    
    
    /**
     * Updates the shared state with the most recent gridstack widget dimensions.
     * @param updatedDimensions A map that maps from GridStackWidget id to a GridStackWidgetDimension
     */
    private void onGridUpdate(final Map<String, GridStackWidgetDimension> updatedDimensions) {
        for(String widgetId : updatedDimensions.keySet()) {
            GridStackWidget widget = getState().getWidgetById(widgetId);
            GridStackWidgetDimension updatedDimension = updatedDimensions.get(widgetId);
            
            widget.setDimension(updatedDimension);
        }
    }

}
