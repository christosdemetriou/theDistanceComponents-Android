package uk.co.thedistance.slimmingworlddemo.recipes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

import uk.co.thedistance.components.PresenterFactory;
import uk.co.thedistance.components.PresenterLoader;
import uk.co.thedistance.components.lists.AbsSortedListItemAdapterDelegate;
import uk.co.thedistance.components.lists.BindingViewHolder;
import uk.co.thedistance.components.lists.ListContent;
import uk.co.thedistance.components.lists.ListPresenter;
import uk.co.thedistance.components.lists.SortedRecyclerListAdapter;
import uk.co.thedistance.components.lists.interfaces.ListPresenterView;
import uk.co.thedistance.slimmingworlddemo.R;
import uk.co.thedistance.slimmingworlddemo.databinding.ActivityRecipesBinding;
import uk.co.thedistance.slimmingworlddemo.databinding.ItemCtacardBinding;
import uk.co.thedistance.slimmingworlddemo.databinding.ItemRecipecardBinding;
import uk.co.thedistance.slimmingworlddemo.rest.RecipesDataSource;
import uk.co.thedistance.slimmingworlddemo.rest.model.Recipe;

public class RecipesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ListPresenter<Recipe, RecipesDataSource>>, ListPresenterView<Recipe> {

    private ListPresenter<Recipe, RecipesDataSource> presenter;
    private ActivityRecipesBinding binding;
    private SortedRecyclerListAdapter<ListSortable> adapter;

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
        binding.recycler.setAdapter(adapter = new RecipesAdapter());
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
    public void showLoading(boolean show, boolean isRefresh) {
        binding.refreshLayout.setRefreshing(show);
    }

    @Override
    public void showContent(ListContent<Recipe> content, boolean refresh) {
        if (content.shouldClear) {
            adapter.clear();
        }

        ArrayList<ListSortable> items = new ArrayList<ListSortable>(content.items);
        items.add(4, new CTA());
        items.add(8, new CTA());

        adapter.addItems(items);
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
    public Loader<ListPresenter<Recipe, RecipesDataSource>> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new RecipesPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<ListPresenter<Recipe, RecipesDataSource>> loader, ListPresenter<Recipe, RecipesDataSource> data) {
        presenter = data;
    }

    @Override
    public void onLoaderReset(Loader<ListPresenter<Recipe, RecipesDataSource>> loader) {
        presenter.onDestroyed();
        presenter = null;
    }

    class RecipesPresenterFactory implements PresenterFactory<ListPresenter<Recipe, RecipesDataSource>> {

        @Override
        public ListPresenter<Recipe, RecipesDataSource> create() {

            RecipesDataSource dataSource = new RecipesDataSource(getApplicationContext(), false);
            return new ListPresenter<>(dataSource);
        }
    }

    class RecipesAdapter extends SortedRecyclerListAdapter<ListSortable> {

        public RecipesAdapter() {
            super(ListSortable.class, sorter);
            delegatesManager.addDelegate(new RecipeItemAdapterDelegate());
            delegatesManager.addDelegate(new RecipeCTAAdapterDelegate());
        }
    }

    class RecipeItemAdapterDelegate extends AbsSortedListItemAdapterDelegate<Recipe, ListSortable, RecipeViewHolder> {

        @Override
        protected boolean isForViewType(@NonNull ListSortable item, SortedList<ListSortable> items, int position) {
            return item instanceof Recipe;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
            ItemRecipecardBinding binding = ItemRecipecardBinding.inflate(getLayoutInflater(), parent, false);
            return new RecipeViewHolder(binding);
        }

        @Override
        protected void onBindViewHolder(@NonNull Recipe item, @NonNull RecipeViewHolder viewHolder) {
            viewHolder.binding.setRecipe(item);
        }
    }

    class RecipeCTAAdapterDelegate extends AbsSortedListItemAdapterDelegate<CTA, ListSortable, CTAViewHolder> {

        @Override
        protected boolean isForViewType(@NonNull ListSortable item, SortedList<ListSortable> items, int position) {
            return item instanceof CTA;
        }

        @NonNull
        @Override
        public CTAViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
            ItemCtacardBinding binding = ItemCtacardBinding.inflate(getLayoutInflater(), parent, false);
            return new CTAViewHolder(binding);
        }

        @Override
        protected void onBindViewHolder(@NonNull CTA item, @NonNull CTAViewHolder viewHolder) {

        }
    }

    class RecipeViewHolder extends BindingViewHolder<ItemRecipecardBinding> {

        public RecipeViewHolder(ItemRecipecardBinding binding) {
            super(binding);
        }
    }

    class CTAViewHolder extends BindingViewHolder<ItemCtacardBinding> {

        public CTAViewHolder(ItemCtacardBinding binding) {
            super(binding);
        }
    }

    class CTA implements ListSortable {

        @Override
        public boolean isSameItem(ListSortable other) {
            return false;
        }

        @Override
        public boolean isSameContent(ListSortable other) {
            return false;
        }

        @Override
        public int compareTo(ListSortable another) {
            return 0;
        }
    }
}
