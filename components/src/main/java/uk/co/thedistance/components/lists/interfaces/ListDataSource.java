package uk.co.thedistance.components.lists.interfaces;

import uk.co.thedistance.components.contentloading.DataSource;
import uk.co.thedistance.components.lists.model.ListContent;

public interface ListDataSource<T> extends DataSource<ListContent<T>> {

    /**
     * When using an {@link uk.co.thedistance.components.lists.presenter.EndlessListPresenter}, the returned
     * value will be used to determine whether {@link #getData()} should be called again when the end of
     * the list is reached
     */
    boolean isListComplete();
}
