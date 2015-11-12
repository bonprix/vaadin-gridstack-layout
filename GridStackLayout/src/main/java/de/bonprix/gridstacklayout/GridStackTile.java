package de.bonprix.gridstacklayout;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

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
 * A component wrapper, styled as a grid tile.
 * 
 * @author Sebastian Funck
 */
public class GridStackTile extends CustomComponent {

    private final VerticalLayout layoutMain;

    private final HorizontalLayout layoutTitlebar;
    private final Label labelTitle;
    private final Label labelMenuHandle;

    private final VerticalLayout content;

    public GridStackTile() {
        setSizeFull();

        this.layoutMain = new VerticalLayout();
        this.layoutMain.addStyleName("v-grid-widget-wrapper");
        this.layoutMain.setHeight("100%");

        // Titlebar
        this.layoutTitlebar = new HorizontalLayout();
        this.layoutTitlebar.setStyleName("v-grid-widget-titlebar");
        this.layoutTitlebar.addStyleName("v-grid-widget-draggable-handle"); // Make the titlebar the draggable handle

        this.labelTitle = new Label("Title");
        this.labelTitle.setWidth("100%");
        this.labelMenuHandle = new Label("#");

        this.layoutTitlebar.addComponent(this.labelTitle);
        this.layoutTitlebar.addComponent(this.labelMenuHandle);

        this.layoutMain.addComponent(this.layoutTitlebar);
        this.layoutMain.setExpandRatio(this.layoutTitlebar, 0.0f);

        // Content
        this.content = new VerticalLayout();
        this.content.setStyleName("v-grid-widget-content");
        this.content.setWidth("100%");
        this.content.setHeightUndefined();

        this.layoutMain.addComponent(this.content);
        this.layoutMain.setExpandRatio(this.content, 1.0f);

        setCompositionRoot(this.layoutMain);
    }

    /**
     * Sets the content of the grid widget.
     * 
     * @param component A component
     */
    public void setContent(final Component component) {
        if (this.content.getComponentCount() != 0) {
            this.content.removeAllComponents();
        }

        this.content.addComponent(component);
    }

}
