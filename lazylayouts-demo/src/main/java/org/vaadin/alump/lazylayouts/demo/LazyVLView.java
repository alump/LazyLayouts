package org.vaadin.alump.lazylayouts.demo;

import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.alump.lazylayouts.LazyComponentProvider;
import org.vaadin.alump.lazylayouts.LazyComponentRequestEvent;
import org.vaadin.alump.lazylayouts.LazyLoadingIndicator;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;

import java.util.Random;

/**
 * Basic test view of LazyVerticalLayout
 */
public class LazyVLView extends VerticalLayout implements View, LazyComponentProvider {

    public final static String VIEW_ID = "lazyvl";

    private Panel panel;
    private LazyVerticalLayout lazyLayout;
    private Navigator navigator;
    private CheckBox singleCB;
    private CheckBox fastCB;
    private CheckBox syncCB;
    private TextField startCountTF;

    private final static int START_WITH = 6;
    private int startWith = START_WITH;

    private final static int MAX_NUMBER_OF_COMPONENTS = 30;

    private Random rand = new Random(0xDEADBEEF);

    public LazyVLView() {
        setSizeFull();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setMargin(true);
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        Button menu = new Button(FontAwesome.BARS.getHtml(), new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(MenuView.VIEW_ID);
            }
        });
        menu.setHtmlContentAllowed(true);
        menu.setDescription("Return to menu");
        buttonLayout.addComponent(menu);

        Button reset = new Button("Reset", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                panel.setScrollTop(0);
                initializeLazyLayout();
            }
        });
        reset.setDescription("Set layout back to original state");
        buttonLayout.addComponent(reset);

        singleCB = new CheckBox("Load one");
        singleCB.setDescription("Only provide one new item at each load");
        singleCB.setImmediate(true);
        singleCB.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                updateIndicatorText();
            }
        });
        buttonLayout.addComponent(singleCB);

        fastCB = new CheckBox("Fast load");
        fastCB.setDescription("Shorter content generation time");
        fastCB.setImmediate(true);
        buttonLayout.addComponent(fastCB);

        syncCB = new CheckBox("No push");
        syncCB.setDescription("Make faked data loading synchronous (blocking)");
        syncCB.setImmediate(true);
        buttonLayout.addComponent(syncCB);

        startCountTF = new TextField();
        startCountTF.setInputPrompt("Count");
        startCountTF.setDescription("How many items are added at reset");
        startCountTF.setValue("" + startWith);
        startCountTF.setImmediate(true);
        startCountTF.setWidth("50px");
        startCountTF.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String string = event.getProperty().getValue().toString();
                try {
                    int newValue = Integer.valueOf(string);
                    if(newValue < 1 || newValue > MAX_NUMBER_OF_COMPONENTS) {
                        throw new NumberFormatException("Invalid number");
                    }
                    startWith = newValue;
                } catch (NumberFormatException e) {
                    event.getProperty().setValue("" + startWith);
                }
            }
        });
        buttonLayout.addComponent(startCountTF);

        panel = new Panel();
        panel.setSizeFull();
        addComponent(panel);
        setExpandRatio(panel, 1.0f);

        lazyLayout = new LazyVerticalLayout();

        // Parent can be also defined manually, but here we trust the automatic resolving
        //lazyLayout.setScrollingParent(panel);

        lazyLayout.setWidth("100%");
        lazyLayout.setStyleName("demoContentLayout");
        lazyLayout.setSpacing(true);
        lazyLayout.setMargin(true);

        panel.setContent(lazyLayout);

        initializeLazyLayout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        navigator = event.getNavigator();
    }

    protected void initializeLazyLayout() {
        lazyLayout.removeAllComponents();

        indexCounter = 0;

        for(int i = 0; i < startWith; ++i) {
            addNewRow(lazyLayout);
        }

        lazyLayout.enableLazyLoading(this);
        updateIndicatorText();
    }

    protected int indexCounter = 0;

    protected void addNewRow(ComponentContainer container) {
        container.addComponent(ComponentGenerator.createComponent(++indexCounter, MAX_NUMBER_OF_COMPONENTS));
    }

    @Override
    public void onLazyComponentRequest(LazyComponentRequestEvent event) {

        // Run sync or async
        if(syncCB.getValue().booleanValue()) {
            new DelayedAddRunnable(event).run();
        } else {
            Thread thread = new Thread(new DelayedAddRunnable(event));
            thread.run();
        }
    }

    private class DelayedAddRunnable implements Runnable {
        private LazyComponentRequestEvent event;

        public DelayedAddRunnable(LazyComponentRequestEvent event) {
            this.event = event;
        }

        @Override
        public void run() {
            // Add delay to act like DB query would be done here
            try {
                int delay;
                if(fastCB.getValue()) {
                    delay = 50;
                } else {
                    delay = 100 + rand.nextInt(900);
                }
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LazyVLView.this.getUI().access(new Runnable() {

                @Override
                public void run() {
                    lazyLoad(event);
                }
            });
        }
    };

    protected void lazyLoad(LazyComponentRequestEvent event) {
        boolean loadMany = !singleCB.getValue().booleanValue();
        // Check how many to add
        int load = loadMany ? 5 : 1;
        if(indexCounter + load >= MAX_NUMBER_OF_COMPONENTS) {
            load = MAX_NUMBER_OF_COMPONENTS - indexCounter;
        }

        // Add new components
        for (int i = 0; i < load; ++i) {
            addNewRow(event.getComponentContainer());
        }

        // Disable when limit is hit
        if(indexCounter >= MAX_NUMBER_OF_COMPONENTS) {
            lazyLayout.disableLazyLoading();
            Notification.show("All demo content loaded. Use reset to start from the beginning :)");
        } else {
            updateIndicatorText();
        }
    }

    /**
     * Example how to update message shown in loading indicator
     */
    protected void updateIndicatorText() {
        LazyLoadingIndicator indicator = (LazyLoadingIndicator)lazyLayout.getLazyLoadingIndicator();
        boolean loadMany = !singleCB.getValue().booleanValue();

        if(loadMany) {
            int last = indexCounter + 5;
            if(last > MAX_NUMBER_OF_COMPONENTS) {
                last = MAX_NUMBER_OF_COMPONENTS;
            }
            indicator.setMessage("Loading " + (indexCounter + 1) + "-" + last + " / " + MAX_NUMBER_OF_COMPONENTS + "...");
        } else {
            indicator.setMessage("Loading " + (indexCounter + 1) + "/" + MAX_NUMBER_OF_COMPONENTS + "...");
        }
    }
}
