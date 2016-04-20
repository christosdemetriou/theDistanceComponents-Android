package uk.co.thedistance.components.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

public class PresenterLoaderHelper<T extends Presenter> implements LoaderManager.LoaderCallbacks<T> {

    private final Context context;
    private final PresenterFactory<T> presenterFactory;
    private T presenter;

    public PresenterLoaderHelper(Context context, PresenterFactory<T> presenterFactory) {
        this.context = context;
        this.presenterFactory = presenterFactory;
    }

    @Override
    public Loader<T> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<T>(context, presenterFactory);
    }

    @Override
    public void onLoadFinished(Loader<T> loader, T data) {
        presenter = data;
    }

    @Override
    public void onLoaderReset(Loader<T> loader) {
        presenter = null;
    }

    public T getPresenter() {
        return presenter;
    }
}
