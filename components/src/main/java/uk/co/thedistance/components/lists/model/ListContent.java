package uk.co.thedistance.components.lists.model;

import java.util.List;

/**
 * Return type for a {@link uk.co.thedistance.components.lists.interfaces.ListDataSource}
 * @param <T> The type of content in the list
 */
public class ListContent<T> {

    public List<T> items;
    public boolean shouldClear;

    /**
     * @param items content items
     * @param shouldClear determines whether the displayed list should be replaced, or appended
     */
    public ListContent(List<T> items, boolean shouldClear) {
        this.items = items;
        this.shouldClear = shouldClear;
    }
}
