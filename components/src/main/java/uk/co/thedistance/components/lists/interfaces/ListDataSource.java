package uk.co.thedistance.components.lists.interfaces;

import java.util.List;

import rx.Observable;

public interface ListDataSource<T extends Listable> {

    Observable<List<T>> getData(boolean refresh);
    void contentDelivered(List<T> items);

    boolean isListComplete();
}
