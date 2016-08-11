package uk.co.thedistance.components.uploading.interfaces;

import rx.Observable;

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
    Observable<RT> getUpload();
}
