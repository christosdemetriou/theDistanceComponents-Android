package uk.co.thedistance.components.lists;

import java.util.ArrayList;

import uk.co.thedistance.components.ContentLoadingPresenter;
import uk.co.thedistance.components.DataSource;
import uk.co.thedistance.components.lists.interfaces.Listable;

public class ListPresenter<T extends Listable> extends ContentLoadingPresenter<ListContent<T>> {

    public ListPresenter(DataSource<ListContent<T>> dataSource) {
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

    }
}
