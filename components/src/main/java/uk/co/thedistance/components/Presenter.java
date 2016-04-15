/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components;

public interface Presenter<T extends PresenterView> {

    void onViewAttached(T view);
    void onViewDetached();
    void onDestroyed();

}
