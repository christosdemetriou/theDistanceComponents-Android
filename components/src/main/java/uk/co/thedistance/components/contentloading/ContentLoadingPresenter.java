package uk.co.thedistance.components.contentloading;

import android.util.Log;

import rx.Subscriber;
import rx.Subscription;
import uk.co.thedistance.components.base.Presenter;

/**
 * Standard implementation for a {@link Presenter} which loads content into a View
 * When {@link #onViewAttached(ContentLoadingPresenterView)} is called, content in memory will be delivered;
 * if not available, we will attempt to load from the {@link DataSource}
 *
 * @param <T> The content type to be loaded
 * @param <DS> The {@link DataSource} responsible for loading content
 * @param <PV> The view responsible for displaying content
 */
public class ContentLoadingPresenter<T, DS extends DataSource<T>, PV extends ContentLoadingPresenterView<T>> implements Presenter<PV> {

    protected PV view;
    protected T content;
    DS dataSource;
    protected Subscription dataSubscription;

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
        view.showLoading(true, refresh);

        dataSubscription = dataSource.getData()
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Error: ", e.getLocalizedMessage());
                        view.showLoading(false, refresh);
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(T content) {
                        keepContent(content);
                        view.showContent(content, refresh);
                        view.showLoading(false, refresh);
                    }
                });
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
