package uk.co.thedistance.slimmingworlddemo.rest;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import rx.Observable;
import uk.co.thedistance.slimmingworlddemo.rest.model.Recipe;

/**
 * Created by pharris on 14/01/15.
 */
public class SlimmingWorldData {
    public static final String FILE_RECIPES = "recipes.json";

    private static Gson gsonInternal = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .create();

    static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .registerTypeAdapter(Recipe.class, new DataDeserializer<Recipe>())
            .create();

    private static SlimmingWorldDataClient mAssetsClient;

    interface SlimmingWorldDataClient {
        @GET("/json/recipes.json")
        Observable<List<Recipe>> getRecipes();
    }

    public static SlimmingWorldDataClient getAssetsClient(Context context) {
        if (mAssetsClient == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(LocalClient.ASSETS_ENDPOINT)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setClient(new LocalClient(context, true))
                    .build();
            mAssetsClient = restAdapter.create(SlimmingWorldData.SlimmingWorldDataClient.class);
        }

        return mAssetsClient;
    }


//    public static ArrayList<Recipe> getRecipes(Context context, boolean justVegetarian) {
//        ArrayList<Recipe> result;
//        JsonElement json = mAssetsClient.getJsonFile(FILE_RECIPES);
//        final JsonArray array = json.getAsJsonArray();
//        result = new ArrayList<Recipe>(array.size());
//        String iterationString;
//        for (JsonElement jsonElement : array) {
//            iterationString = jsonElement.getAsJsonObject().toString();
//            final Recipe recipe = gson.fromJson(iterationString, Recipe.class);
//            if (!justVegetarian || recipe.vegetarian) {
//                // only add vegetarian recipes if requested
//                result.add(recipe);
//            }
//        }
//        return result;
//    }

//    public static Recipe getRecipe(Context context, String recipeName) {
//
//        final JsonElement json = mAssetsClient.getJsonFile(FILE_RECIPES);
//        final JsonArray array = json.getAsJsonArray();
//        String iterationString;
//        for (JsonElement jsonElement : array) {
//            iterationString = jsonElement.getAsJsonObject().toString();
//            final Recipe recipe = gson.fromJson(iterationString, Recipe.class);
//            if (null != recipeName && recipeName.equals(recipe.pageName)) {
//                // name matches
//                return recipe;
//            }
//        }
//
//        return null;
//    }

    public static class DataDeserializer<T> implements JsonDeserializer<T> {

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            T object;

            object = gsonInternal.fromJson(json, typeOfT);

            Field[] fields = object.getClass().getDeclaredFields();

            for (Field field : fields) {
                Annotation annotation = field.getAnnotation(JsonRequired.class);
                if (annotation != null) {
                    try {
                        Object value = field.get(object);
                        if (value == null
                                || (value instanceof String && TextUtils.isEmpty((String) value))
                                || (value instanceof Collection && ((Collection) value).isEmpty())) {

                            throw new DataException(object, field);
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
            }
            return object;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface JsonRequired {
    }

    public static class DataException extends JsonParseException {
        public DataException(Object object, Field field) {
            super("Parsing " + object.getClass().getSimpleName() + " - " + getIdentifier(object) + "\n" + field.getName() + " is invalid");
        }

        private static String getIdentifier(Object object) {
            StringBuilder idBuilder = new StringBuilder(20);

            if (object instanceof Recipe) {
                Recipe recipe = (Recipe) object;
                if (recipe.title != null) {
                    idBuilder.append(recipe.title);
                }
                if (recipe.pageName != null) {
                    comma(idBuilder);
                    idBuilder.append(recipe.pageName);
                }
                if (recipe.recipeCode != null) {
                    comma(idBuilder);
                    idBuilder.append(recipe.recipeCode);
                } else {
                    idBuilder.append("No RecipeCode");
                }
            }

            return idBuilder.length() == 0 ? "no identifier" : idBuilder.toString();
        }

        private static void comma(StringBuilder builder) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
        }

        public DataException(String message) {
            super(message);
        }
    }
}
