package org.vaadin.alump.lazylayouts;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;

/**
 * LazyLoadingIndicator is default implementation used by LazyVerticalLayout to indicate lazy loading
 */
public class LazyLoadingIndicator extends CssLayout {

    protected ProgressBar progressBar;
    protected Label messageLabel;

    protected final static String DEFAULT_MESSAGE = "Loading...";

    /**
     * Create new lazy loading indicator with default message
     */
    public LazyLoadingIndicator() {
        this(null);
    }

    /**
     * Create new lazy loading indicator with given message
     * @param message Message shown to user
     */
    public LazyLoadingIndicator(String message) {
        addStyleName("lazy-loading");

        progressBar = new ProgressBar();
        progressBar.addStyleName("lazy-loading-pbar");
        progressBar.setIndeterminate(true);
        addComponent(progressBar);

        messageLabel = new Label(message != null ? message : DEFAULT_MESSAGE);
        messageLabel.addStyleName("lazy-loading-message");
        addComponent(messageLabel);
    }

    /**
     * Access ProgressBar of indicator component
     * @return
     */
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * Access message Label of indicator component
     * @return
     */
    public Label getMessageLabel() {
        return messageLabel;
    }

    /**
     * Define message shown in indicator
     * @param message
     */
    public void setMessage(String message) {
        messageLabel.setValue(message);
    }

    /**
     * Get current message shown in indicator
     */
    public String getMessage() {
        return messageLabel.getValue();
    }
}
