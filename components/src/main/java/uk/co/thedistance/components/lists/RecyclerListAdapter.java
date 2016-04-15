package uk.co.thedistance.components.lists;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.co.thedistance.components.lists.interfaces.ListAdapter;
import uk.co.thedistance.components.lists.interfaces.ListItemPresenter;
import uk.co.thedistance.components.lists.interfaces.Listable;

public class RecyclerListAdapter<T extends Listable<T>> extends RecyclerView.Adapter<ViewHolder> implements ListAdapter<T> {

    final ListItemPresenter<T> itemPresenter;
    SortedList<Listable> items = new SortedList<>(Listable.class, new SortedList.Callback<Listable>() {
        @Override
        public int compare(Listable o1, Listable o2) {
            return o1.compareTo(o2);
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
        public boolean areContentsTheSame(Listable oldItem, Listable newItem) {
            return oldItem.isSameContent(newItem);
        }

        @Override
        public boolean areItemsTheSame(Listable item1, Listable item2) {
            return item1.isSameItem(item2);
        }
    });

    public RecyclerListAdapter(ListItemPresenter<T> itemPresenter) {
        this.itemPresenter = itemPresenter;
    }

    @Override
    public int getItemViewType(int position) {
        return itemPresenter.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return itemPresenter.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = (T) items.get(position);
        itemPresenter.bindItem(holder, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void addItems(List<T> items) {
        this.items.addAll(new ArrayList<Listable>(items));
    }

    @Override
    public void clear() {
        items.clear();
    }
}
