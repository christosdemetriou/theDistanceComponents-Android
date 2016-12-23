package uk.co.thedistance.components.uploading;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import uk.co.thedistance.components.base.Presenter;
import uk.co.thedistance.components.contentloading.ContentLoadingPresenterView;
import uk.co.thedistance.components.contentloading.DataSource;
import uk.co.thedistance.components.uploading.interfaces.DataUploader;
import uk.co.thedistance.components.uploading.interfaces.UploadingPresenterView;

/**
 *
 * @param <T>  The content type to be uploaded
 * @param <RT> The response type
 * @param <DU> The {@link DataUploader} responsible for uploading content
 * @param <UV> The view responsible for displaying response
 */
public class UploadingPresenter<T, RT, DU extends DataUploader<T, RT>, UV extends UploadingPresenterView<RT>> implements Presenter<UV> {

    protected UV view;
    public DU dataUploader;
    protected Subscription dataSubscription;

    public UploadingPresenter(DU dataUploader) {
        this.dataUploader = dataUploader;
    }

    public void setDataUploader(DU dataUploader) {
        this.dataUploader = dataUploader;
    }

    /**
     * This method will attempt to load content from the {@link DataSource} and call the appropriate methods
     * on the {@link ContentLoadingPresenterView} in the event of errors or completion
     *
     */
    public void uploadContent(T content) {
        unsubscribe();

        dataUploader.setContent(content);

        showLoading(true);

        dataSubscription = dataUploader.getUpload()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RT>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Error: ", e.getLocalizedMessage());
                        showLoading(false);
                        view.showError(e, e.getLocalizedMessage());
                        dataSubscription = null;
                    }

                    @Override
                    public void onNext(RT response) {
                        view.uploadComplete(response);
                        showLoading(false);
                        dataSubscription = null;
                    }
                });
    }

    private void showLoading(boolean show) {
        view.showUploading(show);
    }

    private void unsubscribe() {
        if (dataSubscription != null && !dataSubscription.isUnsubscribed()) {
            dataSubscription.unsubscribe();
        }
    }

    @Override
    public void onViewAttached(UV view) {
        this.view = view;

        if (isUploading()) {
            view.showUploading(true);
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

    private boolean isUploading() {
        return dataSubscription != null;
    }
}
