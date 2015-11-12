package de.bonprix.gridstacklayout;

import java.lang.reflect.Method;

import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;
import com.vaadin.util.ReflectTools;

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
 * Listener interface for grid widget resizing.
 * 
 * @author Sebastian Funck
 */
public interface GridWidgetResizeListener {

    public static class GridWidgetResizeEvent extends Event {

        private final Component target;
        private final GridStackWidgetDimension dimension;

        
        public GridWidgetResizeEvent(final GridStackLayout grid, final Component component, final GridStackWidgetDimension dimension) {
            super(grid);
            this.target = component;
            this.dimension = dimension;
        }

        public Component getTarget() {
            return target;
        }

        public GridStackWidgetDimension getDimension() {
            return dimension;
        }
    }

    public static final Method componentResizeMethod = ReflectTools.findMethod(GridWidgetResizeListener.class, "onComponentResize", GridWidgetResizeEvent.class);

    void onComponentResize(final GridWidgetResizeEvent event);

}
