package uk.co.thedistance.components.analytics;

import android.os.Handler;
import android.support.v4.app.Fragment;

import uk.co.thedistance.components.analytics.model.AnalyticEvent;

/**
 * Extend this class to enable thedistancekit functionality.
 * <p/>
 * <ul>
 * <li>set a screen name to automatically send Google Analytics screen views</li>
 * <li>send an analytic event using {@link #sendEvent(AnalyticEvent)}</li>
 * </ul>
 */
public abstract class AnalyticsFragment extends Fragment {

    abstract String getScreenName();

    abstract boolean isTrackingEnabled();

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isVisible()) {
                    sendScreen();
                }
            }
        }, 200);
    }

    protected void sendScreen() {
        sendScreen(false);
    }

    protected void sendScreen(boolean newSession) {
        if (!isTrackingEnabled()) {
            return;
        }
        // Send screen if fragment still visible (to avoid sending home every time backstack cleared)
        Analytics.sendScreen(getActivity(), getScreenName(), newSession);
    }

    protected void sendEvent(AnalyticEvent event) {
        Analytics.send(getActivity(), event);
    }
}
