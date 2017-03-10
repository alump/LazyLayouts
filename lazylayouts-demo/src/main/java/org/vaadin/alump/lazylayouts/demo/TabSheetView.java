package org.vaadin.alump.lazylayouts.demo;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.vaadin.alump.lazylayouts.LazyComponentContainer;
import org.vaadin.alump.lazylayouts.LazyComponentProvider;
import org.vaadin.alump.lazylayouts.LazyComponentRequestEvent;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;

public class TabSheetView extends VerticalLayout implements View, LazyComponentProvider {

    public final static String VIEW_ID = "tabsheet";

    private final static int MAX_COMPONENT_COUNT = 40;

    private Navigator navigator;

    public TabSheetView() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidth(100, Unit.PERCENTAGE);
        addComponent(buttonLayout);

        Button menu = new Button(VaadinIcons.MENU.getHtml(), event -> {
            navigator.navigateTo(MenuView.VIEW_ID);
        });
        menu.setCaptionAsHtml(true);
        buttonLayout.addComponent(menu);

        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        addComponent(tabSheet);
        setExpandRatio(tabSheet, 1f);

        addWrappedLazyLayout(tabSheet);
        addWrappedLazyLayout(tabSheet);
        addWrappedLazyLayout(tabSheet);
    }

    private void addWrappedLazyLayout(TabSheet tabs) {
        int index = tabs.getComponentCount();
        String caption = "Layout #" + index;

        Panel panel = new Panel();
        panel.setSizeFull();
        tabs.addTab(panel, caption);

        LazyVerticalLayout layout = new LazyVerticalLayout();
        layout.setCaption(caption);
        layout.setMargin(true);
        layout.setWidth(100, Unit.PERCENTAGE);
        layout.enableLazyLoading(this);
        for(int i = 1; i <= 10; ++i) {
            layout.addComponent(ComponentGenerator.createComponent(i, MAX_COMPONENT_COUNT));
        }
        panel.setContent(layout);
    }

    @Override
    public void onLazyComponentRequest(LazyComponentRequestEvent event) {
        LazyComponentContainer layout = event.getComponentContainer();
        for(int i = 0; i < 5 &&  layout.getComponentCount() <= MAX_COMPONENT_COUNT; ++i) {
            layout.addComponent(ComponentGenerator.createComponent(layout.getComponentCount(), MAX_COMPONENT_COUNT));
        }
        if(layout.getComponentCount() >= MAX_COMPONENT_COUNT) {
            layout.disableLazyLoading();
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.navigator = event.getNavigator();
    }
}
