package uk.co.thedistance.components;

public interface ContentLoadingPresenterView<T> {

    void showLoading(boolean show, boolean isRefresh);

    void showContent(T content, boolean refresh);

    void showError(String error);
}
