package uk.co.thedistance.components.analytics;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import uk.co.thedistance.components.analytics.interfaces.AnalyticsTracker;
import uk.co.thedistance.components.analytics.model.AnalyticEvent;
import uk.co.thedistance.components.analytics.model.AnalyticSession;
import uk.co.thedistance.components.analytics.model.ScreenEvent;
import uk.co.thedistance.components.analytics.model.TimingEvent;

/**
 * Helper class for Analytics events and screen views.
 * <p>
 *
 * @author benbaggley
 */
public class Analytics {

    public static final String PREFS_FIELD_SEND_USAGE = "send_usage";
    private static final String PREFS_FIELD_LAST_SCREEN = "last_screen";

    private String sCurrentScreen;
    private SharedPreferences sPreferences;

    protected final AnalyticsTracker tracker;
    protected boolean enabled = true;

    public Analytics(Application application, AnalyticsTracker tracker) {
        this.tracker = tracker;
        this.sPreferences = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void newSession(AnalyticSession session) {
        tracker.setSession(session);
    }

    public void send(AnalyticEvent event) {
        if (!enabled) {
            return;
        }

        tracker.sendEvent(event);
    }

    public void send(TimingEvent event) {
        if (!enabled) {
            return;
        }

        tracker.sendTiming(event);
    }

    public void send(ScreenEvent event) {
        if (!enabled) {
            return;
        }
        if (sCurrentScreen == null) {
            sCurrentScreen = sPreferences
                    .getString(PREFS_FIELD_LAST_SCREEN, null);
        }

        if (sCurrentScreen == null || !sCurrentScreen.equals(event.getScreenName())) {

            tracker.sendScreen(event.getScreenName());
            sCurrentScreen = event.getScreenName();

            sPreferences.edit()
                    .putString(PREFS_FIELD_LAST_SCREEN, sCurrentScreen)
                    .apply();
        }
    }

}
