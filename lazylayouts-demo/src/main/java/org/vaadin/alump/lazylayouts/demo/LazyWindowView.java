package org.vaadin.alump.lazylayouts.demo;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.alump.lazylayouts.LazyComponentProvider;
import org.vaadin.alump.lazylayouts.LazyComponentRequestEvent;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;

import java.util.Random;

import static org.vaadin.alump.lazylayouts.demo.ComponentGenerator.createComponent;

/**
 * Tests to use LazyVerticalLayout inside Windows
 */
public class LazyWindowView extends LazyVerticalLayout implements View, LazyComponentProvider {
    public final static String VIEW_ID = "window";

    protected int windowCounter = 0;
    private Navigator navigator = null;

    private final static int MAX_NUMBER_OF_COMPONENTS = 30;

    private Random rand = new Random(0xDEADBEEF);

    public LazyWindowView() {
        setSpacing(true);
        setMargin(true);

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        addComponent(buttons);

        Button menu = new Button(FontAwesome.BARS.getHtml(), new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(MenuView.VIEW_ID);
            }
        });
        menu.setHtmlContentAllowed(true);
        buttons.addComponent(menu);

        Button openWindow = new Button("Open Window", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                openWindow(++windowCounter);
            }
        });
        buttons.addComponent(openWindow);
    }

    private void openWindow(int windowIndex) {
        final Window window = new Window();
        window.setWidth("400px");
        window.setHeight("80%");
        window.setCaption("Lazy Inside Window #" + windowIndex);


        LazyVerticalLayout lazyLayout = new LazyVerticalLayout();
        lazyLayout.setMargin(true);
        lazyLayout.setSpacing(true);
        lazyLayout.setData(new Integer(0));
        lazyLayout.enableLazyLoading(this);
        window.setContent(lazyLayout);

        int windowIndexCounter = 0;

        for(int i = 0; i < 8; ++i) {
            lazyLayout.addComponent(createComponent(++windowIndexCounter, MAX_NUMBER_OF_COMPONENTS));
        }

        lazyLayout.setData(new Integer(windowIndexCounter));

        UI.getCurrent().addWindow(window);
        window.setPositionX(windowIndex * 10);
        window.setPositionY(windowIndex * 10);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        navigator = event.getNavigator();
    }

    @Override
    public void onLazyComponentRequest(LazyComponentRequestEvent event) {

        // Just to resolve layout specific counter used to limit generated items
        // in this demo
        LazyVerticalLayout casted = (LazyVerticalLayout)event.getComponentContainer();
        int value = ((Integer)casted.getData()).intValue();

        // Small delay to make thing look more natural
        try {
            Thread.sleep(100 + rand.nextInt(300));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        event.getComponentContainer().addComponent(createComponent(++value, MAX_NUMBER_OF_COMPONENTS));
        if(value >= MAX_NUMBER_OF_COMPONENTS) {
            event.getComponentContainer().disableLazyLoading();
        }

        // Storing demo counter value back to layout
        casted.setData(new Integer(value));
    }
}
