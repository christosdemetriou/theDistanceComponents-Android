package uk.co.thedistance.components.lists;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import uk.co.thedistance.components.lists.interfaces.ListDataSource;

public class EndlessListPresenter<T, DS extends ListDataSource<T>> extends ListPresenter<T, DS> {

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    private int endOffset = 1;

    public EndlessListPresenter(final DS dataSource, RecyclerView recyclerView) {
        super(dataSource);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        });
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
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - endOffset;
        }

        return false;
    }
}
