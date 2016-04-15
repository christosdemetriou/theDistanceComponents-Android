package uk.co.thedistance.components;

public interface ContentLoadingPresenterView<T> {

    void showLoading(boolean show);

    void showContent(T content);

    void showError(String error);
}
