/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.pocketseodemo.model;

/**
 * Created by pharris on 17/02/16.
 */
public interface MozScapeLink extends MozScape {

    String getUrl();

    String getTitle();

    String getAnchorText();

    boolean isNoFollow();
}
