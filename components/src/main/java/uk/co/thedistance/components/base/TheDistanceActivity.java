package uk.co.thedistance.components.base;

import android.support.v7.app.AppCompatActivity;


/**
 * Extend this class to enable thedistancekit functionality.
 */
abstract public class TheDistanceActivity extends AppCompatActivity {

    private boolean activityDestroyed = false;
    private boolean activityPaused = false;

    @Override
    protected void onResume() {
        super.onResume();
        activityPaused = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

        activityPaused = true;
    }


    @Override
    protected void onDestroy() {
        activityDestroyed = true;

        super.onDestroy();
    }

    /**
     * Check if the activity has been, or will be, destroyed
     */
    public boolean isActivityUnavailable() {
        return activityDestroyed || isFinishing();
    }

    /**
     * Check if the activity is paused
     */
    public boolean isPaused() {
        return activityPaused;
    }
}
