package uk.co.thedistance.components.pocketseodemo;

import android.text.TextUtils;

import java.util.List;

public class StringUtils {

    /**
     * Append the given separator String to the StringBuilder if not empty.
     *
     * @param stringBuilder The builder to append
     * @param separator     The String to be appended
     */
    public static void separator(StringBuilder stringBuilder, String separator) {
        if (stringBuilder.length() > 0) {
            stringBuilder.append(separator);
        }
    }

    /**
     * Append ", " to a StringBuilder, if not empty
     *
     * @param stringBuilder The builder to append
     */
    public static void comma(StringBuilder stringBuilder) {
        separator(stringBuilder, ", ");
    }

    /**
     * Join a list of objects together in a separated String, using the toString() method for each objects
     *
     * @param objects   The objects to join together
     * @param separator The String to be used to separate objects
     * @return The separated String
     */
    public static String join(List<?> objects, String separator) {
        if (objects.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(objects.size() * objects.get(0).toString().length());
        for (Object object : objects) {
            String objString = object.toString();
            if (TextUtils.isEmpty(objString)) {
                continue;
            }
            separator(builder, separator);
            builder.append(objString);
        }
        return builder.toString();
    }
}
