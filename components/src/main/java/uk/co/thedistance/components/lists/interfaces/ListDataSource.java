package uk.co.thedistance.components.lists.interfaces;

import java.util.List;

import uk.co.thedistance.components.DataSource;
import uk.co.thedistance.components.lists.ListContent;

public interface ListDataSource<T extends Listable> extends DataSource<ListContent<T>> {

    void contentDelivered(List<T> items);

    boolean isListComplete();
}
