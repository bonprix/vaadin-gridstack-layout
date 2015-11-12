package de.bonprix.gridstacklayout.client;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.AbstractLayoutState;

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
 * The shared state.
 * 
 * @author Sebastian Funck
 */
public class GridStackLayoutState extends AbstractLayoutState {

	private static final long serialVersionUID = -667130558066195851L;

	/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                         State Fields                       */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Determines if the grid is in stacked mode.
     */
    public boolean isStackedMode = false;

    /**
     * Determines if the grid is in read-only mode.
     */
    public boolean isReadOnly = false;

    /**
     * Determines when the grid automatically switches into stacked mode.
     */
    public int stackedModeWidth = 400;

    /**
     * Contains all gridstack widgets that are present in the grid.
     */
    public List<GridStackWidget> children = new ArrayList<GridStackWidget>();

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /*                     Public Getter/Setter                   */
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Adds the <i>widget</i> to the state.
     * @param widget The widget
     */
    public void addGridStackWidget(final GridStackWidget widget) {
        this.children.add(widget);
    }

    /**
     * Removes the <i>widget</i> from the state.
     * @param widget The widget
     */
    public void removeGridStackWidget(final GridStackWidget widget) {
        this.children.remove(widget);
    }

    /**
     * Returns the GridStackWidget by its connector.
     * @param connector The connector
     * @return The GridStackWidget
     * 
     * @throws IllegalArgumentException if <i>connector</i> is not attached to the grid
     */
    public GridStackWidget getWidgetByConnector(final Connector connector) {
        for(GridStackWidget widget : this.children) {
            if (widget.getConnector().equals(connector)) {
                return widget;
            }
        }
        
        throw new IllegalArgumentException("Given connector is not connected to the grid!");
    }

    /**
     * Returns the GridStackWidget by its id.
     * @param widgetId The widget id
     * @return The GridStackWidget
     * 
     * @throws IllegalArgumentException if no GridStackWidget is associated with id
     */
    public GridStackWidget getWidgetById(final String widgetId) {
        for(GridStackWidget widget : this.children) {
            if (widget.getId().equals(widgetId)) {
                return widget;
            }
        }
        
        throw new IllegalArgumentException("No widget found in the grid that matche the given id '" + widgetId + "' !");
    }

}