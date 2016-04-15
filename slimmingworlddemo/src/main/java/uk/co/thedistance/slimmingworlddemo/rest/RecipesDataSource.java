package uk.co.thedistance.slimmingworlddemo.rest;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.thedistance.components.lists.interfaces.ListDataSource;
import uk.co.thedistance.slimmingworlddemo.rest.model.Recipe;

public class RecipesDataSource implements ListDataSource<Recipe> {

    boolean vegetarian = false;
    Context context;
    SlimmingWorldData.SlimmingWorldDataClient client;

    public RecipesDataSource(Context context, boolean vegetarian) {
        this.vegetarian = vegetarian;
        client = SlimmingWorldData.getAssetsClient(context);
    }

    @Override
    public Observable<List<Recipe>> getData(boolean refresh) {
        return client.getRecipes()
                .flatMap(new Func1<List<Recipe>, Observable<Recipe>>() {
                    @Override
                    public Observable<Recipe> call(List<Recipe> recipes) {
                        return Observable.from(recipes);
                    }
                })
                .filter(new Func1<Recipe, Boolean>() {
                    @Override
                    public Boolean call(Recipe recipe) {
                        return !vegetarian || recipe.vegetarian;
                    }
                }).toList()
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public void contentDelivered(List<Recipe> items) {

    }

    @Override
    public boolean isListComplete() {
        return false;
    }
}
