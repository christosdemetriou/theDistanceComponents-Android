/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.base;

public interface PresenterFactory<T extends Presenter> {

    T create();
}
