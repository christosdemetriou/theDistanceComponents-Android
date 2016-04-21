package uk.co.thedistance.components.analytics.interfaces;

import uk.co.thedistance.components.analytics.model.AnalyticEvent;
import uk.co.thedistance.components.analytics.model.TimingEvent;

public interface AnalyticsTracker {

    void sendScreen(String screenName);

    void sendEvent(AnalyticEvent event);

    void sendTiming(TimingEvent event);
}
