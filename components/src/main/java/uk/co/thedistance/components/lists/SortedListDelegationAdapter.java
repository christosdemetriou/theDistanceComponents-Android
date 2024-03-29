package uk.co.thedistance.components.lists;

import android.support.v7.util.SortedList;

import com.hannesdorfmann.adapterdelegates2.AbsDelegationAdapter;

import java.util.ArrayList;
import java.util.List;

import uk.co.thedistance.components.lists.interfaces.Sortable;


public class SortedListDelegationAdapter<T extends Sortable> extends AbsDelegationAdapter<SortedList<T>> {

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    final Sorter sorter;

    public SortedListDelegationAdapter(Class<T> clazz, final Sorter sorter) {
        this.sorter = sorter;
        items = new SortedList<T>(clazz, new SortedList.Callback<T>() {
            @Override
            public int compare(T o1, T o2) {
                return sorter.compare(o1, o2);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return sorter.areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areItemsTheSame(T item1, T item2) {
                return sorter.areItemsTheSame(item1, item2);
            }
        });
    }

    public void addItems(List<? extends T> items) {
        this.items.addAll(new ArrayList<>(items));
    }

    public void addItem(T item) {
        this.items.add(item);
    }

    public void removeItem(T item) {
        this.items.remove(item);
    }

    public void clear() {
        items.clear();
    }
}
