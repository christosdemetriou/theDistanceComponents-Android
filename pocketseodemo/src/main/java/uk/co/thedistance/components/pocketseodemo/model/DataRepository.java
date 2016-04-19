/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.pocketseodemo.model;

import java.util.List;

import rx.Observable;
import uk.co.thedistance.components.pocketseodemo.mozscape.model.MSLinkFilter;

/**
 * Created by pharris on 17/02/16.
 */
public interface DataRepository {

    interface Callback<T> {
        void success(T data);
        void error(String message);
    }

    Observable<List<MozScapeLink>> getLinkMetrics(String url, int page, MSLinkFilter filter, boolean refresh);

}
