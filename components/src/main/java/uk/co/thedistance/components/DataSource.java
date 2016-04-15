package uk.co.thedistance.components;

import rx.Observable;

public interface DataSource<T> {

    Observable<T> getData(boolean refresh);
}
