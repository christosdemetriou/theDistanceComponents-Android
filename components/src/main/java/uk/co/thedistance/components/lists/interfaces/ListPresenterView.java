package uk.co.thedistance.components.lists.interfaces;

import uk.co.thedistance.components.ContentLoadingPresenterView;
import uk.co.thedistance.components.lists.ListContent;

public interface ListPresenterView<T> extends ContentLoadingPresenterView<ListContent<T>> {

    void showEmpty(boolean show);
}
