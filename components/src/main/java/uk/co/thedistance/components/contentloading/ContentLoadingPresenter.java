package uk.co.thedistance.components.contentloading;

import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ViewTreeObserver;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
    protected T content;
    protected DS dataSource;
    protected Subscription dataSubscription;
    private SwipeRefreshLayout refreshLayout;

    public ContentLoadingPresenter(DS dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onViewAttached(PV view) {
        this.view = view;
        if (content != null) {
            view.showContent(content, true);
        } else {
            loadContent(true);
        }
    }

    public void onViewAttached(final PV view, final SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadContent(true);
            }
        });
        refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    refreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    refreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                onViewAttached(view);
            }
        });
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

        if (refresh) {
            dataSource.reset();
        }
        showLoading(true, refresh);

        dataSubscription = dataSource.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Error: ", e.getLocalizedMessage());
                        showLoading(false, refresh);
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(T content) {
                        keepContent(content);
                        view.showContent(content, refresh);
                        showLoading(false, refresh);
                    }
                });
    }

    private void showLoading(boolean show, boolean isRefresh) {
        if (isRefresh && refreshLayout != null) {
            refreshLayout.setRefreshing(show);
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
        if (dataSubscription != null && !dataSubscription.isUnsubscribed()) {
            dataSubscription.unsubscribe();
        }
    }
}
