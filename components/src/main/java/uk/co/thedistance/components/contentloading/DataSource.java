package uk.co.thedistance.components.contentloading;

import rx.Observable;

public interface DataSource<T> {

    /**
     * Any configuration such as paging, caching etc should be cleared
     */
    void reset();

    /**
     * Provide a pre-configured observable, observing on the main thread. Any manipulation of data
     * should be done here
     */
    Observable<T> getData();
}
