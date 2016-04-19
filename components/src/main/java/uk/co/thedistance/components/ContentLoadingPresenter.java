package uk.co.thedistance.components;

import android.util.Log;

import rx.Subscriber;
import rx.Subscription;

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

    protected void keepContent(T content) {
        this.content = content;
    }

    private void unsubscribe() {
        if (dataSubscription != null && !dataSubscription.isUnsubscribed()) {
            dataSubscription.unsubscribe();
        }
    }
}
