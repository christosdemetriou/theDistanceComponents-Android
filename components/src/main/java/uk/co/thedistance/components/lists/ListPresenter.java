package uk.co.thedistance.components.lists;

import java.util.ArrayList;

import uk.co.thedistance.components.ContentLoadingPresenter;
import uk.co.thedistance.components.lists.interfaces.ListDataSource;
import uk.co.thedistance.components.lists.interfaces.ListPresenterView;

public class ListPresenter<T, DS extends ListDataSource<T>> extends ContentLoadingPresenter<ListContent<T>, DS, ListPresenterView<T>> {


    public ListPresenter(DS dataSource) {
        super(dataSource);
    }

    @Override
    protected void keepContent(ListContent<T> content) {
        if (this.content == null) {
            this.content = new ListContent<>(new ArrayList<T>(), true);
        }

        if (content.shouldClear) {
            this.content.items.clear();
        }
        this.content.items.addAll(content.items);

        view.showEmpty(this.content.items.isEmpty());
    }
}
