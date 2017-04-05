package uk.co.thedistance.components.contentloading;

import io.reactivex.Observable;

public interface DataSource<T> {

    /**
     * Any configuration such as paging, caching etc should be cleared
     */
    void reset();

    /**
     * Provide a pre-configured observable. Any manipulation of data
     * should be done here
     */
    Observable<T> getData();
}
