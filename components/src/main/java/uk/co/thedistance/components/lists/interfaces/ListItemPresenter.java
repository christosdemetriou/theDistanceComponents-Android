package uk.co.thedistance.components.lists.interfaces;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import uk.co.thedistance.components.BR;

public abstract class ListItemPresenter<T> {

    public abstract int getItemViewType(int position);

    public abstract RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType);

    public void bindItem(RecyclerView.ViewHolder holder, T item) {
        ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        if (binding != null) {
            binding.setVariable(BR.listable, item);
        }
    }
}
