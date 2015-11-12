package de.bonprix.gridstacklayout.client;

import com.vaadin.shared.Connector;

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
 * Represents the state of a grid widget.
 * 
 * @author Sebastian Funck
 */
public class GridStackWidget {

    private String id;
    private boolean isDraggable;
    private boolean isResizable;
    private Connector connector;
    private GridStackWidgetDimension position;

    public GridStackWidget() {
        this.id = "none";
        this.isDraggable = true;
        this.isResizable = true;
        this.position = new GridStackWidgetDimension();
    }

    public GridStackWidget(final String id, final Connector connector, final GridStackWidgetDimension position) {
        this();
        this.id = id;
        this.connector = connector;
        this.position = position;
    }

    public GridStackWidgetDimension getDimension() {
        return this.position.clone();
    }

    public void setDimension(final GridStackWidgetDimension position) {
        this.position = position.clone();
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Connector getConnector() {
        return this.connector;
    }

    public void setConnector(final Connector connector) {
        this.connector = connector;
    }

    public boolean isDraggable() {
        return this.isDraggable;
    }

    public void setDraggable(final boolean isDraggable) {
        this.isDraggable = isDraggable;
    }

    public boolean isResizable() {
        return this.isResizable;
    }

    public void setResizable(final boolean isResizable) {
        this.isResizable = isResizable;
    }

}
