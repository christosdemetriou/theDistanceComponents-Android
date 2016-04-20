package uk.co.thedistance.components.lists;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * A simple {@link android.support.v7.widget.RecyclerView.ViewHolder} implementation that accepts a
 * {@link ViewDataBinding} instead of a {@link android.view.View}
 * @param <T> The type of binding
 */
public class BindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public T binding;

    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
