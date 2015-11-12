package de.bonprix.gridstacklayout.client;

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
 * Client-side listener for grid events.
 * 
 * @author Sebastian Funck
 */
public interface GridStackListener {

    /**
     * Called when a widget has been added.
     * 
     * @param widgetId The widget id
     */
    public void onWidgetAdded(final String widgetId);

    /**
     * Called when a widget has been removed.
     * 
     * @param widgetId The widget id
     */
    public void onWidgetRemoved(final String widgetId);

    /**
     * Called when a widget has been resized.
     * 
     * @param widgetId The widget id
     */
    public void onWidgetResized(final String widgetId);

    /**
     * Called when a widget has been dragged.
     * 
     * @param widgetId The widget id
     */
    public void onWidgetDragged(final String widgetId);

    /**
     * Called when the grid height has changed.
     * 
     * @param height The height in pixels
     */
    public void onHeightChanged(int height);

}
