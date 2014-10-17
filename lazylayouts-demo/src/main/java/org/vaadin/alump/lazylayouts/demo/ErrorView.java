package org.vaadin.alump.lazylayouts.demo;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;

/**
 * ErrorView is also used to test lazy vertical layout (in mode where lazyloading is not used at all)
 */
public class ErrorView extends LazyVerticalLayout implements View {

    private Navigator navigator;

    public ErrorView() {
        setSpacing(true);
        setMargin(true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        navigator = event.getNavigator();

        Label errorLabel = new Label("404 View Not Found");
        errorLabel.addStyleName("error-header");
        addComponent(errorLabel);

        errorLabel = new Label("Unknown view: '" + event.getNavigator().getState() + "'");
        errorLabel.addStyleName("error-desc");
        addComponent(errorLabel);

        Button menuButton = new Button("Return to menu", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(MenuView.VIEW_ID);
            }
        });
        addComponent(menuButton);
    }
}
