package uk.co.thedistance.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * An extension of the {@link WebView}  class which can be used in a {@link android.widget.ScrollView}
 */
public class WebViewFixWrap extends WebView {


    public WebViewFixWrap(Context context) {
        super(context);

        setup();
    }

    private void setup() {
        if (!isInEditMode()) {
            setWebViewClient(new WebviewClientFixWrap());
            getSettings().setJavaScriptEnabled(true);
            getSettings().setLoadWithOverviewMode(true);
        }
    }

    public WebViewFixWrap(Context context, AttributeSet attrs) {
        super(context, attrs);

        setup();
    }

    public WebViewFixWrap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setup();
    }

    public static class WebviewClientFixWrap extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            view.setLayoutParams(params);
        }
    }

}