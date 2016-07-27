package uk.co.thedistance.components.lists;

import java.util.Comparator;

import uk.co.thedistance.components.lists.interfaces.Sortable;

public abstract class Sorter implements Comparator<Sortable> {
    boolean areItemsTheSame(Sortable lhs, Sortable rhs) {
        return lhs.isSameItem(rhs);
    }

    boolean areContentsTheSame(Sortable lhs, Sortable rhs) {
        return lhs.isSameContent(rhs);
    }
}
