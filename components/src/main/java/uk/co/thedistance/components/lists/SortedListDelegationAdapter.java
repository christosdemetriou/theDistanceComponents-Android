package uk.co.thedistance.components.lists;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;

import com.hannesdorfmann.adapterdelegates2.AbsDelegationAdapter;
import com.hannesdorfmann.adapterdelegates2.AdapterDelegatesManager;

public class SortedListDelegationAdapter<T extends SortedList<?>> extends AbsDelegationAdapter<T> {

    public SortedListDelegationAdapter() {
    }

    public SortedListDelegationAdapter(@NonNull AdapterDelegatesManager<T> delegatesManager) {
        super(delegatesManager);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
}