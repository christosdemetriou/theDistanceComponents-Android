package uk.co.thedistance.components.lists;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates2.AdapterDelegate;

import java.util.List;


public abstract class AbsSortedListItemAdapterDelegate<I extends T, T, VH extends RecyclerView.ViewHolder>
    implements AdapterDelegate<SortedList<T>> {

  @Override public final boolean isForViewType(@NonNull SortedList<T> items, int position) {
    return isForViewType(items.get(position), items, position);
  }

  @Override public final void onBindViewHolder(@NonNull SortedList<T> items, int position,
      @NonNull RecyclerView.ViewHolder holder) {
    onBindViewHolder((I) items.get(position), (VH) holder);
  }

  /**
   * Called to determine whether this AdapterDelegate is the responsible for the given item in the list or not
   * element
   * @param item The item from the list at the given position
   * @param items The items from adapters dataset
   * @param position The items position in the dataset (list)
   * @return true if this AdapterDelegate is responsible for that, otherwise false
   */
  protected abstract boolean isForViewType(@NonNull T item, SortedList<T> items, int position);

  /**
   * Creates the  {@link RecyclerView.ViewHolder} for the given data source item
   * @param parent The ViewGroup parent of the given datasource
   * @return ViewHolder
   */
  @NonNull @Override public abstract VH onCreateViewHolder(@NonNull ViewGroup parent);

  /**
   * Called to bind the {@link RecyclerView.ViewHolder} to the item of the dataset
   * @param item The data item
   * @param viewHolder The ViewHolder
   */
  protected abstract void onBindViewHolder(@NonNull I item, @NonNull VH viewHolder);
}
