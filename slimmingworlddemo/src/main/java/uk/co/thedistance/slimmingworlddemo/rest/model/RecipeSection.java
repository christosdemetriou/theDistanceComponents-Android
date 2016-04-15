package uk.co.thedistance.slimmingworlddemo.rest.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by benbaggley on 17/04/15.
 */
public class RecipeSection implements Parcelable {

    String title;
    public ArrayList<String> ingredients;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeSerializable(this.ingredients);
    }

    public RecipeSection() {
    }

    private RecipeSection(Parcel in) {
        this.title = in.readString();
        this.ingredients = (ArrayList<String>) in.readSerializable();
    }

    public static final Creator<RecipeSection> CREATOR = new Creator<RecipeSection>() {
        public RecipeSection createFromParcel(Parcel source) {
            return new RecipeSection(source);
        }

        public RecipeSection[] newArray(int size) {
            return new RecipeSection[size];
        }
    };

    public String getTitle() {
        String formatted = title.substring(0, 1).toUpperCase() + title.substring(1).trim();
        if (!formatted.endsWith(":")) {
            formatted += ":";
        }
        return formatted;
    }
}
