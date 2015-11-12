package de.bonprix.gridstacklayout.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.StyleConstants;

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
 * The client side widget.
 * 
 * @author Sebastian Funck
 */
public class GridStackLayoutWidget extends ComplexPanel {

    public static final int DEFAULT_COLUMNS = 9;

    public static final String CLASSNAME = "v-gridstacklayout";

    private final Element gridstackDiv;
    private final String gridstackId;
    private JavaScriptObject gridstack;
    private JavaScriptObject gridstackResizeInterval;

    private int gridstackMinWidth;

    private boolean gridstackInMinWidthMode = false;

    private JavaScriptObject gridstackTempStyle;
    private JavaScriptObject gridstackTilesTempStyle;

    private boolean initialized = false;

    private boolean gridstackReadOnly;

    public int i = 0;

    private GridStackListener listener;

    public GridStackLayoutWidget() {
        this.gridstackId = DOM.createUniqueId();

        this.gridstackDiv = DOM.createDiv();
        this.gridstackDiv.addClassName("grid-stack");
        this.gridstackDiv.setId(this.gridstackId);
        setElement(this.gridstackDiv);

        // Clear any unwanted styling
        final Style style = getElement().getStyle();
        style.setBorderStyle(BorderStyle.NONE);
        style.setMargin(0, Unit.PX);
        style.setPadding(0, Unit.PX);

        if (BrowserInfo.get()
                       .isIE()) {
            style.setPosition(Position.RELATIVE);
        }

        setStyleName(CLASSNAME);

        addAttachHandler(new Handler() {
            @Override
            public void onAttachOrDetach(final AttachEvent event) {
                checkInit();
            }
        });

        setStackedModeWidth(60 * DEFAULT_COLUMNS);
    }

    @Override
    public void setStyleName(final String style) {
        super.setStyleName(style);
        addStyleName(StyleConstants.UI_LAYOUT);
    }

    private Widget getWidgetByElement(final Element elem) {
        for (Widget w : getChildren()) {
            if (w.getElement()
                 .getId()
                 .equals(elem.getId())) {
                return w;
            }
        }

        return null;
    }

    public GridStackLayoutWidget setServerRpc(final GridStackListener listener) {
        this.listener = listener;
        return this;
    }

    private void checkInit() {
        if (this.initialized || !isAttached() /* || this.config == null */) {
            return;
        }

        this.initialized = true;
        this.gridstackReadOnly = false;
        this.gridstackInMinWidthMode = false;
        initNative(this.gridstackId, DEFAULT_COLUMNS, 150); // sfunck TODO: Extract to config
    }

    private void onDrag(final String id) {
        this.listener.onWidgetDragged(id);
        this.listener.onHeightChanged(getGridHeight());
    }

    private void onResize(final String id) {
        this.listener.onWidgetResized(id);
        this.listener.onHeightChanged(getGridHeight());
    }

    private void onGridModeChanged(final Boolean isMinWidthMode) {
        this.gridstackInMinWidthMode = isMinWidthMode;
        // this.listener.onGridModeChanged(isMinWidthMode); // TODO: Update state
    }

    private native void initNative(final String gridstackId, final int columns, final int cellHeight)
    /*-{    
     // console.log("debug - initWidget() - start");
     
     //-- Constructor
     //----------------------------------------------------------------

     var self = this;
     var $grid = $wnd.$("#" + gridstackId);
     
     this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackResizeInterval = 'undefined';
     
     $grid.addClass("grid-stack");
     $grid.gridstack({
        width: columns,
        cell_height: cellHeight,
        item_class: "v-grid-widget",
        handle: ".v-grid-widget",
        draggable: {
            handle: '.v-grid-widget-draggable-handle', 
            scroll: true,
            appendTo: 'body'
        },
        always_show_resize_handle: /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent),
        resizable: {
            handles: 'se'
        }
     });

     this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack = $grid.data('gridstack');

     //-- Widget Dragging listener
     //----------------------------------------------------------------

     $grid.on("dragstop", function (event, ui) {
         // console.log("debug - dragStop()");
     
         // sfunck TODO: delay the onDrag event so that gridstack has time to update the attributes. This is a dirty hack, search for a better solution
         $wnd.setTimeout(function() {
             self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.commit();
             self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::onDrag(Ljava/lang/String;)(event.target.id);
         }, 100);
         
         // console.log("debug - dragStop() - end");
     });

     //-- Widget Resize listener
     //----------------------------------------------------------------

     $grid.on("resizestop", function (event, ui) {
         // console.log("debug - resizeStop()");
         
         // sfunck TODO: delay the onResize event so that gridstack has time to update the attributes. This is a dirty hack, search for a better solution
         $wnd.setTimeout(function() {
             self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.commit();
             self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::onResize(Ljava/lang/String;)(event.target.id);
         }, 100);

         // console.log("debug - resizeStop() - end");
     });
     
     //-- Converts a style string into it's object representation, to use it with jquery's .css() function
     //----------------------------------------------------------------

     this.convertStyleToCss = function (style) {
        var tmpArr = style.split(";");
        var css = {};

        for (var i = 0; i < tmpArr.length; i++) {
            if (tmpArr[i].indexOf(":") >= 0) {
                var tmpLine = tmpArr[i].split(":");
                var key = tmpLine[0].trim().replace('"', '');
                var value = tmpLine[1].trim().replace('"', '');

                css[key] = value;
            }
        }

        return css;
    };
    
     //-- Enables the 'MinWidth'-mode
     //----------------------------------------------------------------
    
    this.enableMinWidthMode = function() {
        // console.log("debug - enableMinWidthMode()");
        
        // Disable dragging/resizing in 'MinWidth'-Mode
        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.disable();
        
        // Get the grid
        var $grid = $wnd.$("#" + self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackId);

        // Save the current styles for later restoring
        var styleGrid = $grid.attr("style");
        
        var styleTiles;
        styleTiles = $grid.children(":first-child").attr("style");

        if (styleGrid === undefined) {
            styleGrid = "";
        }
        
        if (styleTiles === undefined) {
            styleTiles = "";
        }

        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackTempStyle = self.convertStyleToCss(styleGrid);
        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackTilesTempStyle = self.convertStyleToCss(styleTiles);

        console.log("Saved style: " + self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackTempStyle);

        // Add 'MinWidth'-Mode styles
        $grid.addClass("stacked");

        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.batch_update();
        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.commit(); // Refresh the grid

        // Set java states
        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::onGridModeChanged(Ljava/lang/Boolean;)(@java.lang.Boolean::TRUE);
        // console.log("debug - enableMinWidthMode() - end");
    }


    //-- Disables the 'MinWidth'-mode
    //----------------------------------------------------------------

    this.disableMinWidthMode = function() {
        // console.log("debug - disableMinWidthMode()");
        // Enable dragging/resizing if not read-only
        if (!this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackReadOnly) {
            self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.enable();
        }

        // Remove 'MinWidth'-Mode styles
        $grid.removeClass("stacked");
        
        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.batch_update();
        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.commit(); // Refresh the grid
        
        // Set java states
        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::onGridModeChanged(Ljava/lang/Boolean;)(@java.lang.Boolean::FALSE);
        // console.log("debug - disableMinWidthMode() - end");
    }
            
    // console.log("debug - initWidget() - end");
    }-*/;

    // ADD & REMOVE WIDGETS
    // -----------------------------------------------------------
    public void addWidget(final GridStackWidget gridWidget, final Widget widget) {
        checkInit();

        addNativeWidget(gridWidget.getId(), 
                        gridWidget.getDimension().getX(), 
                        gridWidget.getDimension().getY(), 
                        gridWidget.getDimension().getWidth(), 
                        gridWidget.getDimension().getHeight());

        add(widget, DOM.getElementById(gridWidget.getId()));
        this.listener.onHeightChanged(getGridHeight());
    }

    private native void addNativeWidget(final String id, final int col, final int row, final int sizeX, final int sizeY)
    /*-{
      // console.log("debug - addNativeWidget()");
      var $widget = $wnd.$("<div id=" + id + ">")
                      .addClass("v-grid-widget");

      this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.add_widget($widget);
      
      // console.log("debug - addNativeWidget() - end");
    }-*/;

    public void removeWidget(final String id) {
        checkInit();
        removeNativeWidget(id);
        this.listener.onHeightChanged(getGridHeight());
    }

    private native void removeNativeWidget(final String id)
    /*-{
      // console.log("debug - removeNativeWidget()");

      var removeWidget = function(gridId, widgetId)
      {
          $wnd.setTimeout(function()
          {
              console.log("Id-Grid: " + gridId);
              console.log("Id-Wid|get: " + widgetId);
          
              var $grid   = $wnd.$("#" + gridId);
              var $widget = $wnd.$("#" + widgetId);
              
              console.log("Remove-Grid: " + $grid);
              console.log("Remove-Wid|get: " + $widget);
              
              $widget.removeClass("v-grid-widget");
              $grid.data("gridstack").remove_widget($widget);
          }, 100);
      }

      removeWidget(this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackId, id);
      
      // console.log("debug - removeNativeWidget() - end");
    }-*/;

    // POSITION & SIZE
    // -----------------------------------------------------------

    public GridStackWidgetDimension getPositions(final String widgetId) {
        final Element e = DOM.getElementById(widgetId);

        if (e == null) {
            return null;
        }

        final int x = Integer.parseInt(e.getAttribute("data-gs-x"));
        final int y = Integer.parseInt(e.getAttribute("data-gs-y"));
        final int width = Integer.parseInt(e.getAttribute("data-gs-width"));
        final int height = Integer.parseInt(e.getAttribute("data-gs-height"));

        return new GridStackWidgetDimension(x, y, width, height);
    }

    public void setWidgetDimension(final String widgetId, final GridStackWidgetDimension position) {
        final Element e = DOM.getElementById(widgetId);

        if (e == null) {
            return;
        }

        setWidgetPosition(widgetId, position.getX(), position.getY());
        setWidgetSize(widgetId, position.getWidth(), position.getHeight());
    }

    private native void setWidgetPosition(final String widgetId, final int x, final int y)
    /*-{
        // console.log("debug - setWidgetPosition(" + widgetId + ", " + x + ", " + y + ")");
        $widget = $wnd.$("#" + widgetId);
        
        if ($widget !== 'undefined' && x >= 0 && y >= 0) {
            this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.move($widget, x, y);
        }
        
        // console.log("debug - setWidgetPosition() - end");
      }-*/;

    private native void setWidgetSize(final String widgetId, final int width, final int height)
    /*-{
        // console.log("debug - setWidgetSize(" + widgetId + ", " + width + ", " + height + ")");
        $widget = $wnd.$("#" + widgetId);
        
        // console.log("debug - " + widgetId + " | " + $widget + " | " + width + " | " + height);
        
        if ($widget !== 'undefined' && width > 0 && height > 0) {
            this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.resize($widget, width, height);
        }
        
        // console.log("debug - setWidgetSize() - end");
      }-*/;

    // RESIZABLE
    // -----------------------------------------------------------

    public native void setWidgetResizable(final String widgetId, boolean resizable)
    /*-{
         // console.log("debug - setResizable(" + widgetId + ", " + resizable + ")");
         $widget = $wnd.$("#" + widgetId);

         if ($widget !== 'undefined') {
             this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.resizable($widget, resizable);        
         }
         // console.log("debug - setResizable() - end");
      }-*/;

    // DRAGGABLE
    // -----------------------------------------------------------

    public native void setWidgetDraggable(final String widgetId, boolean movable)
    /*-{
         // console.log("debug - setDraggable(" + widgetId + ", " + movable + ")");
         $widget = $wnd.$("#" + widgetId);

         if ($widget !== 'undefined') {
             this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.movable($widget, movable);
         }
         // console.log("debug - setDraggable() - end");
      }-*/;

    public native void setReadOnly(boolean isReadOnly)
    /*-{
        // console.log("debug - isReadOnly(" + isReadOnly + ")");
        this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackReadOnly = isReadOnly;
        
        if (!isReadOnly) {
            this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.enable();
        } else {
            this.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstack.disable();
        }
        // console.log("debug - isReadOnly() - end");
     }-*/;

    public void setStackedModeWidth(final int minWidth) {
        this.gridstackMinWidth = minWidth;
        updateMinWidthCallback();
    }

    private native void updateMinWidthCallback()
    /*-{
        // console.log("debug - setMinWidth()");

        var self = this;
       
        if (self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackResizeInterval !== undefined) {
            window.clearInterval(self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackResizeInterval);
        }
        
        //-- Callback function that checks for grid width changes to 
        //-- enable/disable 'MinWidth'-mode accordenly to the grid's width
        //----------------------------------------------------------------
        var minWidthFunction = function() {
            var $grid = $wnd.$("#" + self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackId);
        
            var minWidth = self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackMinWidth;

            if ($grid.width() <= minWidth && !self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackInMinWidthMode) {
                console.log("=> MinWidthMode (grid: " + $grid.width() + ", minwidth: " + minWidth + ", mode: " + self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackInMinWidthMode + ")");
                self.enableMinWidthMode();
            }

            else if ($grid.width() > minWidth && self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackInMinWidthMode) {
                console.log("<= MinWidthMode (grid: " + $grid.width() + ", minwidth: " + minWidth + ", mode: " + self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackInMinWidthMode + ")");
                self.disableMinWidthMode();
            }
        };

        self.@de.bonprix.gridstacklayout.client.GridStackLayoutWidget::gridstackResizeInterval = $wnd.setInterval(minWidthFunction, 100);

        // console.log("debug - setMinWidth() - end");

     }-*/;

    private int getGridHeight() {
        String heightString = this.gridstackDiv.getStyle()
                                               .getHeight();

        return Integer.valueOf(heightString.replace("px", ""));
    }
}