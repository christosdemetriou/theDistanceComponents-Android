/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;

/**
 * An implementation of a synchronous loader, used to instantiate and retain a {@link Presenter}
 * throughout the {@link android.app.Activity} lifecycle
 * If called in {@link Activity#onCreate(Bundle)}, presenter will be ready in {@link Activity#onResume()}
 * If called in {@link android.support.v4.app.Fragment#onActivityCreated(Bundle)}, presenter will be ready in {@link Fragment#onResume()}
 * @param <T>
 */
public class PresenterLoader<T extends Presenter> extends Loader<T> {

    T presenter;
    final PresenterFactory<T> presenterFactory;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     * @param presenterFactory
     */
    public PresenterLoader(Context context, PresenterFactory<T> presenterFactory) {
        super(context);
        this.presenterFactory = presenterFactory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();


        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        deliverResult(presenter = presenterFactory.create());
    }

    @Override
    protected void onReset() {
        super.onReset();

        presenter.onDestroyed();
        presenter = null;
    }
}
