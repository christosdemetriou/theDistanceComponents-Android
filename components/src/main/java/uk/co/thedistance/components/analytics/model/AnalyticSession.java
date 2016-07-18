package uk.co.thedistance.components.analytics.model;

import java.util.HashMap;

public class AnalyticSession {

    private final HashMap<Integer, String> customDimensions;

    public AnalyticSession(HashMap<Integer, String> customDimensions) {
        this.customDimensions = customDimensions;
    }

    public HashMap<Integer, String> getCustomDimensions() {
        return customDimensions;
    }
}
