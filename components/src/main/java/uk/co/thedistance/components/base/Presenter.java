/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.base;

public interface Presenter<T> {

    /**
     * Call this method when the view is ready to receive content
     * @param view
     */
    void onViewAttached(T view);

    /**
     * Call this method when the view is no longer ready. Any pending actions on the view should be
     * cancelled
     */
    void onViewDetached();

    /**
     * Call this method when the Presenter is no longer needed and any clean up actions can be taken
     */
    void onDestroyed();

}
