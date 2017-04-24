package uk.co.thedistance.components.uploading.interfaces;

import io.reactivex.Single;

public interface DataUploader<T, RT> {

    /**
     * Any configuration should be cleared
     */
    void reset();

    void setContent(T content);

    /**
     * Provide an observable. Any manipulation of data
     * should be done here
     */
    Single<RT> getUpload();
}
