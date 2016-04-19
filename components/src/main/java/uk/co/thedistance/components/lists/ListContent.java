package uk.co.thedistance.components.lists;

import java.util.List;

public class ListContent<T> {

    public List<T> items;
    public boolean shouldClear;

    public ListContent(List<T> items, boolean shouldClear) {
        this.items = items;
        this.shouldClear = shouldClear;
    }
}
