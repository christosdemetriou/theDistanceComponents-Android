package uk.co.thedistance.components.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import uk.co.thedistance.components.uploading.UploadingPresenter;

public class UploaderLoaderHelper<T extends UploadingPresenter> implements LoaderManager.LoaderCallbacks<T> {

    private final Context context;
    private final UploaderFactory<T> uploaderFactory;
    private T uploader;

    public UploaderLoaderHelper(Context context, UploaderFactory<T> uploaderFactory) {
        this.context = context;
        this.uploaderFactory = uploaderFactory;
    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return new UploaderLoader<>(context, uploaderFactory);
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T data) {
        uploader = data;
    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
        uploader = null;
    }

    public T getUploader() {
        return uploader;
    }
}
