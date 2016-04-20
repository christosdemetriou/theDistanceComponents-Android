package uk.co.thedistance.components.analytics;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;
import java.util.Map;

import uk.co.thedistance.components.analytics.interfaces.AnalyticsApplication;
import uk.co.thedistance.components.analytics.model.AnalyticEvent;
import uk.co.thedistance.components.analytics.model.TimingEvent;

/**
 * Helper class for Google Analytics events and screen views.
 * <p>
 * Apps using this class must implement {@link AnalyticsApplication} in their {@link Application}
 * class in order to configure a {@link Tracker} and provide any custom dimensions
 *
 * @author benbaggley
 */
public class Analytics {

    public static final String PREFS_FIELD_SEND_USAGE = "send_usage";
    private static final String PREFS_FIELD_LAST_SCREEN = "last_screen";

    private static String sCurrentScreen;
    private static SharedPreferences sPreferences;

    public static AnalyticsApplication getApp(Context context) {
        Application application = (Application) context.getApplicationContext();
        if (application instanceof AnalyticsApplication) {
            return (AnalyticsApplication) application;
        }
        throw new IllegalArgumentException("Application class must implement AnalyticsApplication");
    }

    public static Tracker getTracker(Context context) {
        return getApp(context).getTracker();
    }

    public static HashMap<Integer, String> getCustomDimensions(Context context) {
        return getApp(context).getCustomDimensions();
    }

    public static void send(Context context, Map<String, String> eventMap) {
        Tracker tracker = getTracker(context);
        if (tracker != null) {
            tracker.send(eventMap);
        }
    }

    public static void send(Context context, AnalyticEvent event) {
        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(event.category)
                .setAction(event.action)
                .setLabel(event.label);

        if (event.startNewSession) {
            builder.setNewSession();
        }

        HashMap<Integer, String> dimensions = getCustomDimensions(context);
        if (dimensions != null) {
            for (int key : dimensions.keySet()) {
                builder.setCustomDimension(key, dimensions.get(key));
            }
        }

        send(context, builder.build());
    }

    public static void send(Context context, TimingEvent event) {
        HitBuilders.TimingBuilder builder = new HitBuilders.TimingBuilder()
                .setCategory(event.category)
                .setLabel(event.label)
                .setVariable(event.name)
                .setValue(event.value);

        if (event.startNewSession) {
            builder.setNewSession();
        }

        HashMap<Integer, String> dimensions = getCustomDimensions(context);
        if (dimensions != null) {
            for (int key : dimensions.keySet()) {
                builder.setCustomDimension(key, dimensions.get(key));
            }
        }

        send(context, builder.build());
    }

    private static SharedPreferences getPreferences(Context context) {
        if (sPreferences == null) {
            sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sPreferences;
    }

    public static void sendScreen(Context context, @Nullable String screenName) {
        sendScreen(context, screenName, false);
    }

    public static void sendScreen(Context context, @Nullable String screenName, boolean newSession) {
        if (screenName == null) {
            return;
        }
        if (sCurrentScreen == null) {
            sCurrentScreen = getPreferences(context)
                    .getString(PREFS_FIELD_LAST_SCREEN, null);
        }

        if (newSession || sCurrentScreen == null || !sCurrentScreen.equals(screenName)) {
            Tracker tracker = getTracker(context);
            if (null == tracker) {
                // user might have opted out so don't send track
                return;
            }

            HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder();
            if (newSession) {
                builder.setNewSession();
            }

            HashMap<Integer, String> dimensions = getCustomDimensions(context);
            if (dimensions != null) {
                for (int key : dimensions.keySet()) {
                    builder.setCustomDimension(key, dimensions.get(key));
                }
            }

            tracker.setScreenName(screenName);
            tracker.send(builder.build());
            sCurrentScreen = screenName;

            getPreferences(context).edit()
                    .putString(PREFS_FIELD_LAST_SCREEN, sCurrentScreen)
                    .apply();
        }
    }

}
