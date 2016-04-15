package uk.co.thedistance.components.lists;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import uk.co.thedistance.components.Presenter;
import uk.co.thedistance.components.lists.interfaces.ListDataSource;
import uk.co.thedistance.components.lists.interfaces.ListPresenterView;
import uk.co.thedistance.components.lists.interfaces.Listable;

public class ListPresenter<T extends Listable> implements Presenter<ListPresenterView<T>> {

    ListPresenterView<T> view;
    List<T> items = new ArrayList<>();
    ListDataSource<T> dataSource;
    private Subscription dataSubscription;

    public ListPresenter(ListDataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onViewAttached(ListPresenterView<T> view) {
        this.view = view;
        if (items != null && !items.isEmpty()) {
            view.showResults(items, true);
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
        if (refresh) {
            items.clear();
        }
        dataSubscription = dataSource.getData(refresh)
                .subscribe(new Subscriber<List<T>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showLoading(false);
                        view.showError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<T> ts) {
                        items.addAll(ts);
                        view.showLoading(false);
                        view.showResults(ts, refresh);
                        dataSource.contentDelivered(items);
                    }
                });
    }

    private void unsubscribe() {
        if (dataSubscription != null && !dataSubscription.isUnsubscribed()) {
            dataSubscription.unsubscribe();
        }
    }
}
