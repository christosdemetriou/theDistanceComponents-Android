package uk.co.thedistance.slimmingworlddemo.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import uk.co.thedistance.components.lists.interfaces.Listable;

/**
 * make sure you use UPPER_CAMEL_CASE FieldNamingPolicy
 * Created by pharris on 08/12/14.
 */
public class Recipe implements Parcelable, Listable<Recipe> {
    public String title;
    public String pageName;
    public String globalIntroduction;
    public String shortIntro;
    public ArrayList<String> ingredients;
    public ArrayList<String> method;
    public String tip;
    public boolean vegetarian;
    public int serves;
    public String prepTime;
    public String cookTime;
    public String totalTime;
    public Syns syns;
    @SerializedName("RecipeCode")
    public String recipeCode;
    @SerializedName("HealthyExtraDisplayText")
    public ArrayList<String> healthyExtras;
    public ArrayList<RecipeSection> subSections;
    public ArrayList<Icon> icons;

    @Override
    public Listable.Properties getProperties() {
        return new Listable.Properties(title, shortIntro, pageName, null);
    }

    @Override
    public boolean isSameItem(Recipe other) {
        return recipeCode.equals(other.recipeCode);
    }

    @Override
    public boolean isSameContent(Recipe other) {
        return equals(other);
    }

    @Override
    public int compareTo(Recipe another) {
        return 0;
    }


    public static class Syns implements Parcelable {
        public String extraEasy;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.extraEasy);
        }

        public Syns() {
        }

        private Syns(Parcel in) {
            this.extraEasy = in.readString();
        }

        public static final Creator<Syns> CREATOR = new Creator<Syns>() {
            public Syns createFromParcel(Parcel source) {
                return new Syns(source);
            }

            public Syns[] newArray(int size) {
                return new Syns[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.pageName);
        dest.writeString(this.globalIntroduction);
        dest.writeString(this.shortIntro);
        dest.writeSerializable(this.ingredients);
        dest.writeSerializable(this.method);
        dest.writeString(this.tip);
        dest.writeByte(vegetarian ? (byte) 1 : (byte) 0);
        dest.writeInt(this.serves);
        dest.writeString(this.prepTime);
        dest.writeString(this.cookTime);
        dest.writeString(this.totalTime);
        dest.writeParcelable(this.syns, flags);
        dest.writeString(this.recipeCode);
        dest.writeSerializable(this.healthyExtras);
        dest.writeTypedList(this.subSections);
        dest.writeTypedList(this.icons);
    }

    public Recipe() {
    }

    private Recipe(Parcel in) {
        this.title = in.readString();
        this.pageName = in.readString();
        this.globalIntroduction = in.readString();
        this.shortIntro = in.readString();
        this.ingredients = (ArrayList<String>) in.readSerializable();
        this.method = (ArrayList<String>) in.readSerializable();
        this.tip = in.readString();
        this.vegetarian = in.readByte() != 0;
        this.serves = in.readInt();
        this.prepTime = in.readString();
        this.cookTime = in.readString();
        this.totalTime = in.readString();
        this.syns = in.readParcelable(Syns.class.getClassLoader());
        this.recipeCode = in.readString();
        this.healthyExtras = (ArrayList<String>) in.readSerializable();
        this.subSections = new ArrayList<>();
        in.readTypedList(subSections, RecipeSection.CREATOR);
        this.icons = new ArrayList<>();
        in.readTypedList(icons, Icon.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public static class Icon implements Parcelable {
        public String label;
        public String image;
        public boolean showInList;
        public boolean membersOnly;

        public
        @DrawableRes
        int imageResourceId = -1;
        public String detail = null;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.label);
            dest.writeString(this.image);
            dest.writeByte(showInList ? (byte) 1 : (byte) 0);
            dest.writeByte(membersOnly ? (byte) 1 : (byte) 0);
        }

        public Icon() {
        }

        public Icon(String label, String detail, int imageResourceId) {
            this.label = label;
            this.imageResourceId = imageResourceId;
            this.detail = detail;
            this.membersOnly = false;
            this.showInList = true;
        }

        protected Icon(Parcel in) {
            this.label = in.readString();
            this.image = in.readString();
            this.showInList = in.readByte() != 0;
            this.membersOnly = in.readByte() != 0;
        }

        public static final Creator<Icon> CREATOR = new Creator<Icon>() {
            public Icon createFromParcel(Parcel source) {
                return new Icon(source);
            }

            public Icon[] newArray(int size) {
                return new Icon[size];
            }
        };
    }
}
