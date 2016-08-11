/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;

import uk.co.thedistance.components.uploading.UploadingPresenter;

/**
 * An implementation of a synchronous loader, used to instantiate and retain a {@link Presenter}
 * throughout the {@link Activity} lifecycle
 * If called in {@link Activity#onCreate(Bundle)}, uploader will be ready in {@link Activity#onResume()}
 * If called in {@link Fragment#onActivityCreated(Bundle)}, uploader will be ready in {@link Fragment#onResume()}
 * @param <T>
 */
public class UploaderLoader<T extends UploadingPresenter> extends Loader<T> {

    T uploader;
    final UploaderFactory<T> uploaderFactory;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     * @param uploaderFactory
     */
    public UploaderLoader(Context context, UploaderFactory<T> uploaderFactory) {
        super(context);
        this.uploaderFactory = uploaderFactory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();


        if (uploader != null) {
            deliverResult(uploader);
            return;
        }

        deliverResult(uploader = uploaderFactory.create());
    }

    @Override
    protected void onReset() {
        super.onReset();

        if (uploader != null) {
            uploader = null;
        }
    }
}
