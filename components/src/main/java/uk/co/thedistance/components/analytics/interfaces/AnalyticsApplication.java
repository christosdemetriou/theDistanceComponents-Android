package uk.co.thedistance.components.analytics.interfaces;

import android.support.annotation.Nullable;

import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public interface AnalyticsApplication {

    /**
     * Get the tracker for this application
     * @return returns tracker, or null if one isn't available
     */
    @Nullable
    Tracker getTracker();

    /**
     * Provide any custom dimensions to be sent with Analytics events
     */
    @Nullable
    HashMap<Integer, String> getCustomDimensions();

    /**
     * Re-initialize the tracker, e.g. after a settings change
     */
    void invalidateTracker();
}
