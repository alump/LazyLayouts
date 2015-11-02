package org.vaadin.alump.lazylayouts.demo;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;

/**
 * View containing navigation buttons to all other views
 */
public class MenuView extends VerticalLayout implements View {

    public final static String VALO_BASED_THEME = "valobased";
    public final static String REINDEER_BASED_THEME = "reindeerbased";

    // MenuView is default, so keep empty id
    public final static String VIEW_ID = "";

    private Navigator navigator;
    private ComboBox themeSelector;

    public MenuView() {
        setSpacing(true);
        setMargin(true);

        Label label = new Label("LazyLayouts add-on brings full lazy loading support for layout content "
            + "(including data used to generate view). This is demo and test application of this add-on.");
        addComponent(label);

        Link link = new Link("Project page on GitHub", new ExternalResource("https://github.com/alump/LazyLayouts"));
        addComponent(link);

        addMenuButton("LazyVerticalLayout", "Demo and tests for LazyVerticalLayout", LazyVLView.VIEW_ID);
        addMenuButton("Inside Windows", "LazyLayouts inside Windows", LazyWindowView.VIEW_ID);
        addMenuButton("Inside TabSheet", "LazyLayouts inside TabSheet (ticket #4)", TabSheetView.VIEW_ID);

        themeSelector = new ComboBox();
        themeSelector.setCaption("Theme");
        themeSelector.addItem(VALO_BASED_THEME);
        themeSelector.addItem(REINDEER_BASED_THEME);
        themeSelector.setNullSelectionAllowed(false);
        themeSelector.setTextInputAllowed(false);
        themeSelector.addValueChangeListener(e -> {
            String value = e.getProperty().getValue().toString();
            if(!value.equals(getUI().getTheme())) {
                getUI().setTheme(value);
            }
        });
        addComponent(themeSelector);

    }

    private void addMenuButton(String caption, String description, final String viewId) {
        Button button = new Button(caption, new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                navigator.navigateTo(viewId);
            }
        });
        button.setDescription(description);
        addComponent(button);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        themeSelector.setValue(getUI().getTheme());
        navigator = event.getNavigator();
    }
}
