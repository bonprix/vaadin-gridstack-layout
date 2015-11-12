package de.bonprix.gridstacklayout.demo;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.bonprix.gridstacklayout.GridStackLayout;
import de.bonprix.gridstacklayout.GridStackTile;

@Theme("demo")
@Title("GridStackLayout Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "de.bonprix.gridstacklayout.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(final VaadinRequest request) {

        // Initialize our new UI component
        final GridStackLayout component = new GridStackLayout();
        
        // |1|1|2|
        // |3|4|4|
        // |5|5|5|
        component.addComponent(new GridStackTile(), 0, 0, 2, 1);
        component.addComponent(new GridStackTile(), 2, 0, 1, 1);
        component.addComponent(new GridStackTile(), 0, 1, 1, 1);
        component.addComponent(new GridStackTile(), 1, 1, 2, 1);
        component.addComponent(new GridStackTile(), 0, 2, 3, 2);
        
        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.addComponent(component);
        setContent(layout);

    }

}
