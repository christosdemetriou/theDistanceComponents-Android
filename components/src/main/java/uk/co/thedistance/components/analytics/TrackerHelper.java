package uk.co.thedistance.components.analytics;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.XmlRes;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Helper class for creating a new Google Analytics {@link Tracker}
 */
public class TrackerHelper {

    /**
     * Create a new Google Analytics {@link Tracker}, checking the SEND_USAGE preference
     * @param context SharedPreferences context
     * @param config xml config file for tracker
     * @return new Tracker, if SEND_USAGE true, null if false
     */
    @Nullable  public static Tracker createTracker(@NonNull  Context context, @XmlRes int config) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences.getBoolean(Analytics.PREFS_FIELD_SEND_USAGE, true)) {
            return GoogleAnalytics.getInstance(context).newTracker(config);
        }
        return null;
    }
}
