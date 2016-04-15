package uk.co.thedistance.components.lists.interfaces;

import java.util.List;

import uk.co.thedistance.components.PresenterView;

public interface ListPresenterView<T extends Listable> extends PresenterView<T> {

    void showLoading(boolean show);

    void showResults(List<T> results, boolean shouldClear);

    void showError(String error);

    void showEmpty(boolean show);
}
