package uk.co.thedistance.components.lists.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import uk.co.thedistance.components.lists.interfaces.ListDataSource;
import uk.co.thedistance.components.lists.interfaces.ListPresenterView;

/**
 * An implementation of {@link ListPresenter} that uses {@link android.support.v7.widget.RecyclerView.OnScrollListener}
 * to continuously load content into a {@link uk.co.thedistance.components.lists.interfaces.ListPresenterView}.
 * Only support for RecyclerViews using a {@link LinearLayoutManager} or its subclasses is included. Override
 * {@link #isScrolledToBottom(RecyclerView)} to extend this.
 */
public class EndlessListPresenter<T, DS extends ListDataSource<T>> extends ListPresenter<T, DS> {

    /**
     * Set the offset (from the bottom of the list) of the item that should be visible for more content to be loaded
     * The default value is 0, i.e. when the last item is visible, more content will be loaded
     *
     * @param endOffset
     */
    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    private int endOffset = 0;

    public EndlessListPresenter(final DS dataSource) {
        super(dataSource);
    }

    private final RecyclerView.OnScrollListener SCROLL_LISTENER = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView.isAnimating()) {
                return;
            }

            if (!dataSource.isListComplete() && isScrolledToBottom(recyclerView)) {
                loadNext();
            }
        }
    };

    @Override
    public void onViewAttached(ListPresenterView<T> view) {
        super.onViewAttached(view);

        view.getRecyclerView().addOnScrollListener(SCROLL_LISTENER);
    }

    @Override
    public void onViewDetached() {
        super.onViewDetached();

        view.getRecyclerView().removeOnScrollListener(SCROLL_LISTENER);
    }

    private void loadNext() {
        if (dataSubscription != null && !dataSubscription.isUnsubscribed()) {
            return;
        }

        loadContent(false);
    }

    protected boolean isScrolledToBottom(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1 - endOffset;
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] positions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            int end = recyclerView.getAdapter().getItemCount() - 1 - endOffset;
            for (int i : positions) {
                if (i == end) {
                    return true;
                }
            }
        }

        return false;
    }
}
