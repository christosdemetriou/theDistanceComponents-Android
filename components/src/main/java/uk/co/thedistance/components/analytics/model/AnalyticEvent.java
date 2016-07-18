package uk.co.thedistance.components.analytics.model;

public class AnalyticEvent {
    public String category;
    public String action;
    public String label;

    public AnalyticEvent() {
    }

    public AnalyticEvent(String category, String action, String label) {
        this.category = category;
        this.action = action;
        this.label = label;
    }
}
