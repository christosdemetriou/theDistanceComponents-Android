/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.pocketseodemo.model;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;


import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import uk.co.thedistance.components.pocketseodemo.StringUtils;
import uk.co.thedistance.components.pocketseodemo.mozscape.MSHelper;
import uk.co.thedistance.components.pocketseodemo.mozscape.MSWebService;
import uk.co.thedistance.components.pocketseodemo.mozscape.model.MSLinkFilter;
import uk.co.thedistance.components.pocketseodemo.mozscape.model.MSLinkMetrics;
import uk.co.thedistance.components.pocketseodemo.mozscape.model.MSNextUpdate;
import uk.co.thedistance.components.pocketseodemo.mozscape.model.MSUrlMetrics;

public class DataRepositoryImpl implements DataRepository {

    private LruCache<String, Observable<?>> cachedObservables = new LruCache<>(10);


    private final MSWebService mMozWebService;
    private final MSHelper.Authenticator mMozAuthenticator;

    public DataRepositoryImpl(MSWebService mMozWebService, MSHelper.Authenticator mMozAuthenticator) {
        this.mMozWebService = mMozWebService;
        this.mMozAuthenticator = mMozAuthenticator;
    }

    private Observable<?> getPreparedObservable(Observable<?> unPrepared, String key, boolean useCache) {

        Observable<?> prepared = null;
        if (useCache) {
            prepared = cachedObservables.get(key);
        }

        if (prepared != null) {
            return prepared;
        }

        prepared = unPrepared.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .cache();

        cachedObservables.put(key, prepared);

        return prepared;
    }

    @Override
    public Observable<List<MozScapeLink>> getLinkMetrics(String url, int page, MSLinkFilter filter, boolean refresh) {


//        Observable<List<MSLinkMetrics>> webServiceResponse = mMozWebService.getLinks(url, MSLinkMetrics.getBitmask(), 25, 25 * (page - 1), mMozAuthenticator.getAuthenticationMap());

        String filterString = StringUtils.join(filter.filters, "+");
        Observable<List<MSLinkMetrics>> webServiceResponse = mMozWebService.getLinks(url, MSLinkMetrics.getBitmask(), 25, 25 * (page - 1),
                filter.scope.toString(), filter.sort.toString(), filterString, mMozAuthenticator.getAuthenticationMap());

        return (Observable<List<MozScapeLink>>) getPreparedObservable(webServiceResponse, "links:" + url + "/" + page, !refresh);
    }

    /**
     * Try to work out what the user meant for a URL - try prefixing missing "http://"
     *
     * @param url
     * @return null, or a validated URL
     */
    public static String sanitiseUrl(String url) {
        Uri u = Uri.parse(url);

        String scheme = u.getScheme();
        if (scheme == null) {
            u = Uri.parse("http://" + url);
        }

        scheme = u.getScheme();
        if (!scheme.equals("http") && !scheme.equals("https")) {
            Log.e("UrlSanitise", "Scheme not recognised");
            return null;
        }

        String host = u.getHost();
        if (TextUtils.isEmpty(host)) {
            Log.e("UrlSanitise", "Host not specified");
            return null;
        }

        return u.toString();
    }

}
