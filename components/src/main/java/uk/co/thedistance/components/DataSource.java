package uk.co.thedistance.components;

import rx.Observable;

public interface DataSource<T> {

    void reset();
    Observable<T> getData();
}
