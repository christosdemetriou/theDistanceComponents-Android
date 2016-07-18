package uk.co.thedistance.components.analytics.interfaces;

import uk.co.thedistance.components.analytics.model.AnalyticEvent;
import uk.co.thedistance.components.analytics.model.AnalyticSession;
import uk.co.thedistance.components.analytics.model.TimingEvent;

public abstract class AnalyticsTracker {

    protected AnalyticSession session;

    public abstract void sendScreen(String screenName);

    public abstract void sendEvent(AnalyticEvent event);

    public abstract void sendTiming(TimingEvent event);

    public void setSession(AnalyticSession session) {
        this.session = session;
    }

    public AnalyticSession getSession() {
        return session;
    }
}
