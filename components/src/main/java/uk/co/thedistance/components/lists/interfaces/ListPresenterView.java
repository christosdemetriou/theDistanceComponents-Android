package uk.co.thedistance.components.lists.interfaces;

import android.support.v7.widget.RecyclerView;

import uk.co.thedistance.components.contentloading.ContentLoadingPresenterView;
import uk.co.thedistance.components.lists.model.ListContent;

public interface ListPresenterView<T> extends ContentLoadingPresenterView<ListContent<T>> {

    /**
     * View should provide an "empty state", indicating to the user that no content is available
     */
    void showEmpty(boolean show);

    RecyclerView getRecyclerView();
}
