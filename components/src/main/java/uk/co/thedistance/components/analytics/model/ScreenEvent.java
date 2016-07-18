package uk.co.thedistance.components.analytics.model;

public class ScreenEvent {

    private final String screenName;

    public ScreenEvent(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenName() {
        return screenName;
    }
}
