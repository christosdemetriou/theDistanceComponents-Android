/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.base;

import uk.co.thedistance.components.uploading.UploadingPresenter;

public interface UploaderFactory<T extends UploadingPresenter> {

    T create();
}
