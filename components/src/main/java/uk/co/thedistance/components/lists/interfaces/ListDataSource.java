package uk.co.thedistance.components.lists.interfaces;

import uk.co.thedistance.components.DataSource;
import uk.co.thedistance.components.lists.ListContent;

public interface ListDataSource<T> extends DataSource<ListContent<T>> {

    boolean isListComplete();
}
