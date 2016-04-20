package uk.co.thedistance.components.analytics;

import android.content.Context;
import android.support.v4.view.ViewPager;

import uk.co.thedistance.components.analytics.Analytics;
import uk.co.thedistance.components.analytics.AnalyticsFragment;

/**
 * Extend this class and implement a {@link PageChangeListener}
 * to automatically send Google Analytics screen views for each item in a {@link ViewPager}.
 */
public abstract class AnalyticsPagerFragment extends AnalyticsFragment {

    protected PageChangeListener pageChangeListener;
    private int currentPage = 0;

    @Override
    protected void sendScreen() {
        super.sendScreen();

        if (isScreenAnalyticsDisabled()) {
            return;
        }

        // If fragment uses PageChangeListener, send current page on resume
        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(currentPage);
        }
    }


    protected class PageChangeListener implements ViewPager.OnPageChangeListener {

        private Context context;
        private String[] screenNames;

        public PageChangeListener(Context context, String... screenNames) {
            this.context = context;
            this.screenNames = screenNames;

            pageChangeListener = this;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPage = position;
            if (screenNames.length > position) {
                Analytics.sendScreen(context, screenNames[position]);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
