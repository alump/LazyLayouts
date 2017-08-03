package org.vaadin.alump.lazylayouts.demo;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.vaadin.alump.lazylayouts.LazyComponentRequestEvent;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;

import java.util.concurrent.atomic.AtomicInteger;

public class Issue7View extends VerticalSplitPanel implements View {

    private final static int CREATE_ITEMS = 50;
    public final static String VIEW_ID = "issue7";

    private Navigator navigator;
    private AtomicInteger itemsLeft = new AtomicInteger(CREATE_ITEMS);

    public Issue7View() {
        addComponents(createTopPart(), createBottomPart());
        setSplitPosition(100, Unit.PIXELS);
    }

    private Component createTopPart() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);

        Button backToMenu = new Button(VaadinIcons.MENU);
        backToMenu.addClickListener(e -> navigator.navigateTo(MenuView.VIEW_ID));

        layout.addComponents(backToMenu);
        return layout;
    }

    private Component createBottomPart() {
        Panel panel = new Panel();
        panel.setSizeFull();

        LazyVerticalLayout layout = new LazyVerticalLayout();
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.enableLazyLoading(this::provideItem);
        panel.setContent(layout);

        return panel;
    }

    private void provideItem(LazyComponentRequestEvent event) {
        if(itemsLeft.decrementAndGet() > 0) {
            event.getComponentContainer().addComponent(ComponentGenerator.createComponent(CREATE_ITEMS - itemsLeft.get() + 1, CREATE_ITEMS));
        } else {
            event.getComponentContainer().disableLazyLoading();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        navigator = event.getNavigator();
    }

}
