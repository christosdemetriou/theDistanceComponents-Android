package uk.co.thedistance.components.uploading.interfaces;

import uk.co.thedistance.components.uploading.UploadingPresenter;

/**
 * Defines a standard interface for a view that loads content
 * To be used along with a {@link UploadingPresenter} and {@link DataUploader}
 * @param <RT> The response type
 */
public interface UploadingPresenterView<RT> {

    /**
     * The view should indicate progress to the user
     * @param show
     */
    void showUploading(boolean show);

    /**
     * Response is delivered to the view
     * @param response Upload respons
     */
    void uploadComplete(RT response);

    void showError(Throwable throwable, String error);
}
