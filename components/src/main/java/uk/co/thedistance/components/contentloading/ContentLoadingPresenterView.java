package uk.co.thedistance.components.contentloading;

/**
 * Defines a standard interface for a view that loads content
 * To be used along with a {@link ContentLoadingPresenter} and {@link DataSource}
 * @param <T> The content type to be load
 */
public interface ContentLoadingPresenterView<T> {

    /**
     * The view should indicate progress to the user
     * @param show
     * @param isRefresh Indicates whether loading will refresh all content, or a partial update
     */
    void showLoading(boolean show, boolean isRefresh);

    /**
     * Content is delivered to the view
     * @param content Content to be displayed
     * @param refresh Indicates whether current content should be refreshed, or appended
     */
    void showContent(T content, boolean refresh);

    void showError(Throwable throwable, String error);
}
