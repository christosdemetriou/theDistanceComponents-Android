package uk.co.thedistance.components.lists.interfaces;

import java.util.List;

public interface ListAdapter<T> {

    void addItems(List<T> items);

    void clear();
}
