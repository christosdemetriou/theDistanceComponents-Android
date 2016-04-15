package uk.co.thedistance.components.lists.interfaces;

import android.support.annotation.DrawableRes;

public interface Listable<T> extends Comparable<T>  {

    class Properties {
        public String title;
        public String subtitle;
        public String imageURL;
        public @DrawableRes
        Integer imageResource;

        public Properties() {
        }

        public Properties(String title, String subtitle, String imageURL, Integer imageResource) {
            this.title = title;
            this.subtitle = subtitle;
            this.imageURL = imageURL;
            this.imageResource = imageResource;
        }
    }

    Properties getProperties();

    boolean isSameItem(T other);

    boolean isSameContent(T other);
}