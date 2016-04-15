package uk.co.thedistance.slimmingworlddemo.recipes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.List;

import uk.co.thedistance.components.ContentLoadingPresenterView;
import uk.co.thedistance.components.PresenterFactory;
import uk.co.thedistance.components.PresenterLoader;
import uk.co.thedistance.components.databinding.ItemBaseBinding;
import uk.co.thedistance.components.lists.ListContent;
import uk.co.thedistance.components.lists.ListPresenter;
import uk.co.thedistance.components.lists.RecyclerListAdapter;
import uk.co.thedistance.components.lists.interfaces.ListItemPresenter;
import uk.co.thedistance.components.lists.interfaces.ListPresenterView;
import uk.co.thedistance.slimmingworlddemo.R;
import uk.co.thedistance.slimmingworlddemo.databinding.ActivityRecipesBinding;
import uk.co.thedistance.slimmingworlddemo.rest.RecipesDataSource;
import uk.co.thedistance.slimmingworlddemo.rest.model.Recipe;

public class RecipesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ListPresenter<Recipe>>, ListPresenterView<Recipe> {

    private ListPresenter<Recipe> presenter;
    private ActivityRecipesBinding binding;
    private RecyclerListAdapter<Recipe> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipes);

        getSupportLoaderManager().initLoader(0, null, this);

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadContent(true);
            }
        });
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter = new RecyclerListAdapter<Recipe>(new RecipeItemPresenter()));
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.refreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                presenter.onViewAttached(RecipesActivity.this);
            }
        });
    }

    @Override
    public void showLoading(boolean show) {
        binding.refreshLayout.setRefreshing(show);
    }

    @Override
    public void showContent(ListContent<Recipe> content) {
        if (content.shouldClear) {
            adapter.addItems(content.items);
        }
    }

    @Override
    public void showError(String error) {
        Snackbar.make(binding.coordinator, error, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.loadContent(true);
                    }
                }).show();
    }

    @Override
    public void showEmpty(boolean show) {
        binding.emptyText.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public Loader<ListPresenter<Recipe>> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new RecipesPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<ListPresenter<Recipe>> loader, ListPresenter<Recipe> data) {
        presenter = data;
    }

    @Override
    public void onLoaderReset(Loader<ListPresenter<Recipe>> loader) {
        presenter.onDestroyed();
        presenter = null;
    }

    class RecipesPresenterFactory implements PresenterFactory<ListPresenter<Recipe>> {

        @Override
        public ListPresenter<Recipe> create() {

            RecipesDataSource dataSource = new RecipesDataSource(getApplicationContext(), false);
            return new ListPresenter<Recipe>(dataSource);
        }
    }

    class RecipeItemPresenter extends ListItemPresenter<Recipe> {

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public RecyclerView.ViewHolder createViewHolder(ViewGroup parent, int viewType) {
            ItemBaseBinding binding = ItemBaseBinding.inflate(getLayoutInflater(), parent, false);
            return new RecipeViewHolder(binding.getRoot());
        }

        class RecipeViewHolder extends RecyclerView.ViewHolder {

            public RecipeViewHolder(View itemView) {
                super(itemView);
            }
        }

    }
}
