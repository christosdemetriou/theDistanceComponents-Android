package uk.co.thedistance.components.analytics.model;

public class TimingEvent {
    public String category;
    public String label;
    public String name;
    public long value;

    public TimingEvent() {
    }

    public TimingEvent(String category, String name, String label, long value) {
        this.category = category;
        this.name = name;
        this.label = label;
        this.value = value;
    }
}
