package uk.co.thedistance.components.lists;

import java.util.List;

import uk.co.thedistance.components.lists.interfaces.Listable;

public class ListContent<T extends Listable> {

    public List<T> items;
    public boolean shouldClear;

    public ListContent(List<T> items, boolean shouldClear) {
        this.items = items;
        this.shouldClear = shouldClear;
    }
}
