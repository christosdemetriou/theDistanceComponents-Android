package uk.co.thedistance.components;

import android.util.Log;

import rx.Subscriber;
import rx.Subscription;
import uk.co.thedistance.components.lists.interfaces.Listable;

public class ContentLoadingPresenter<T> implements Presenter<ContentLoadingPresenterView<T>> {

    ContentLoadingPresenterView<T> view;
    protected T content;
    DataSource<T> dataSource;
    private Subscription dataSubscription;

    public ContentLoadingPresenter(DataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onViewAttached(ContentLoadingPresenterView<T> view) {
        this.view = view;
        if (content != null) {
            view.showContent(content);
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

    public void loadContent(final boolean refresh) {
        unsubscribe();

        view.showLoading(true);

        dataSubscription = dataSource.getData(refresh)
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Error: ", e.getLocalizedMessage());
                        view.showLoading(false);
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(T content) {
                        keepContent(content);
                        view.showLoading(false);
                        view.showContent(content);
                    }
                });
    }

    protected void keepContent(T content) {
        this.content = content;
    }

    private void unsubscribe() {
        if (dataSubscription != null && !dataSubscription.isUnsubscribed()) {
            dataSubscription.unsubscribe();
        }
    }
}
