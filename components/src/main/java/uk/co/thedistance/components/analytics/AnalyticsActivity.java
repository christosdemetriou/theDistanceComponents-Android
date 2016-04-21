package uk.co.thedistance.components.analytics;

import android.os.Handler;

import uk.co.thedistance.components.analytics.model.AnalyticEvent;
import uk.co.thedistance.components.base.TheDistanceActivity;

/**
 * Extend this class to enable thedistancekit functionality.
 * <p/>
 * <ul>
 * <li>set a screen name to automatically send Google Analytics screen views</li>
 * <li>send an analytic event using {@link #sendEvent(AnalyticEvent)}</li>
 * </ul>
 */
abstract public class AnalyticsActivity extends TheDistanceActivity {

    protected abstract String getScreenName();

    protected abstract boolean isTrackingEnabled();

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendScreen();
            }
        }, 200);
    }

    protected void sendScreen() {
        if (!isTrackingEnabled()) {
            return;
        }

        Analytics.sendScreen(this, getScreenName());
    }

    protected void sendEvent(AnalyticEvent event) {
        Analytics.send(this, event);
    }
}
