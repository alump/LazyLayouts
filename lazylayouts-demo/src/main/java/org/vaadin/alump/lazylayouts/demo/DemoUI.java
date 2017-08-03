package org.vaadin.alump.lazylayouts.demo;

import com.vaadin.annotations.Push;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

@Theme("valobased")
@Push
@Title("LazyLayouts Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "org.vaadin.alump.lazylayouts.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        Navigator navigator = new Navigator(this, this);
        setNavigator(navigator);

        navigator.setErrorView(ErrorView.class);

        navigator.addView(MenuView.VIEW_ID, MenuView.class);
        navigator.addView(LazyVLView.VIEW_ID, LazyVLView.class);
        navigator.addView(LazyWindowView.VIEW_ID, LazyWindowView.class);
        navigator.addView(TabSheetView.VIEW_ID, TabSheetView.class);
        navigator.addView(Issue7View.VIEW_ID, Issue7View.class);
    }

}
