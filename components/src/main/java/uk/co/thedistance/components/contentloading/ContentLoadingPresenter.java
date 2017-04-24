package uk.co.thedistance.components.contentloading;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.thedistance.components.base.Presenter;

/**
 * Standard implementation for a {@link Presenter} which loads content into a View
 * When {@link #onViewAttached(ContentLoadingPresenterView)} is called, content in memory will be delivered;
 * if not available, we will attempt to load from the {@link DataSource}
 *
 * @param <T>  The content type to be loaded
 * @param <DS> The {@link DataSource} responsible for loading content
 * @param <PV> The view responsible for displaying content
 */
public class ContentLoadingPresenter<T, DS extends DataSource<T>, PV extends ContentLoadingPresenterView<T>> implements Presenter<PV> {

    protected PV view;
    public T content;
    public DS dataSource;
    protected Disposable dataSubscription;
    private SwipeRefreshLayout refreshLayout;

    public ContentLoadingPresenter(DS dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onViewAttached(@NonNull PV view) {
        this.view = view;
        if (content != null) {
            view.showContent(content, false);
        } else {
            loadContent(true);
        }
    }

    public void onViewAttached(final @NonNull PV view, final SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadContent(true);
            }
        });

        onViewAttached(view);
    }

    @Override
    public void onViewDetached() {
        unsubscribe();
    }

    @Override
    public void onDestroyed() {
        unsubscribe();
    }

    public void setDataSource(DS dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * This method will attempt to load content from the {@link DataSource} and call the appropriate methods
     * on the {@link ContentLoadingPresenterView} in the event of errors or completion
     *
     * @param refresh If true, {@link DataSource#reset()} will be called before loading content
     */
    public void loadContent(final boolean refresh) {
        unsubscribe();

        if (view == null) {
            return;
        }

        if (refresh) {
            dataSource.reset();
        }
        showLoading(true, refresh);

        dataSource.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<T>() {
                    @Override
                    public void onError(Throwable e) {
                        Log.d("Error: ", e.getLocalizedMessage());
                        showLoading(false, refresh);
                        view.showError(e, e.getLocalizedMessage());
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        dataSubscription = d;
                    }

                    @Override
                    public void onSuccess(T content) {
                        showLoading(false, refresh);
                        keepContent(content);
                        view.showContent(content, refresh);
                    }
                });
    }

    private void showLoading(boolean show, boolean isRefresh) {
        if (isRefresh && refreshLayout != null) {
            refreshLayout.setRefreshing(show);
            return;
        }
        if (view == null) {
            return;
        }

        view.showLoading(show, isRefresh);
    }

    /**
     * This method is called when new content is loaded, before delivery to the {@link ContentLoadingPresenterView}
     * in order to cache content in memory, for delivery to the view after recreation, i.e. after orientation change
     *
     * @param content
     */
    protected void keepContent(T content) {
        this.content = content;
    }

    private void unsubscribe() {
        if (dataSubscription != null && !dataSubscription.isDisposed()) {
            dataSubscription.dispose();
        }
    }
}
