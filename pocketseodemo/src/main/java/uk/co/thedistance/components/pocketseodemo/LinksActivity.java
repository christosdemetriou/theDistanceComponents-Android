package uk.co.thedistance.components.pocketseodemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import uk.co.thedistance.components.PresenterFactory;
import uk.co.thedistance.components.PresenterLoader;
import uk.co.thedistance.components.lists.AbsSortedListItemAdapterDelegate;
import uk.co.thedistance.components.lists.BindingViewHolder;
import uk.co.thedistance.components.lists.EndlessListPresenter;
import uk.co.thedistance.components.lists.ListContent;
import uk.co.thedistance.components.lists.SortedRecyclerListAdapter;
import uk.co.thedistance.components.lists.interfaces.ListPresenterView;
import uk.co.thedistance.components.pocketseodemo.databinding.ActivityLinksBinding;
import uk.co.thedistance.components.pocketseodemo.databinding.ItemLinkBinding;
import uk.co.thedistance.components.pocketseodemo.databinding.ItemLoadingBinding;
import uk.co.thedistance.components.pocketseodemo.model.DataRepository;
import uk.co.thedistance.components.pocketseodemo.model.MozScapeLink;
import uk.co.thedistance.components.pocketseodemo.viewmodel.MozScapeLinkViewModel;

public class LinksActivity extends AppCompatActivity implements ListPresenterView<MozScapeLink>, LoaderManager.LoaderCallbacks<EndlessListPresenter<MozScapeLink, LinksDataSource>> {


    private ActivityLinksBinding binding;
    private EndlessListPresenter<MozScapeLink, LinksDataSource> presenter;
    private LinksAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_links);

        getSupportLoaderManager().initLoader(0, null, this);

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadContent(true);
            }
        });
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(adapter = new LinksAdapter());
    }

    @Override
    protected void onResume() {
        super.onResume();

        binding.swipeRefresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                binding.swipeRefresh.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                presenter.onViewAttached(LinksActivity.this);
            }
        });
    }

    @Override
    public void showEmpty(boolean show) {
        binding.emptyText.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoading(boolean show, boolean isRefresh) {
        if (isRefresh) {
            binding.swipeRefresh.setRefreshing(show);
        } else {
            adapter.showLoading(show);
        }
    }

    @Override
    public void showContent(ListContent<MozScapeLink> content, boolean refresh) {
        if (refresh) {
            adapter.clear();
        }
        adapter.addItems(content.items);
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public Loader<EndlessListPresenter<MozScapeLink, LinksDataSource>> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, new LinksPresenterLoaderFactory());
    }

    @Override
    public void onLoadFinished(Loader<EndlessListPresenter<MozScapeLink, LinksDataSource>> loader, EndlessListPresenter<MozScapeLink, LinksDataSource> data) {
        presenter = data;
    }

    @Override
    public void onLoaderReset(Loader<EndlessListPresenter<MozScapeLink, LinksDataSource>> loader) {
        presenter.onDestroyed();
        presenter = null;
    }

    class LinksPresenterLoaderFactory implements PresenterFactory<EndlessListPresenter<MozScapeLink, LinksDataSource>> {

        @Override
        public EndlessListPresenter<MozScapeLink, LinksDataSource> create() {
            DataRepository repository = PocketSeoApplication.getApplicationComponent(LinksActivity.this).repository();
            LinksDataSource dataSource = new LinksDataSource(repository, "thedistance.co.uk");
            return new EndlessListPresenter<>(dataSource, binding.recycler);
        }
    }

    class LinkViewHolder extends BindingViewHolder<ItemLinkBinding> {

        public LinkViewHolder(ItemLinkBinding binding) {
            super(binding);
        }
    }

    class LoadingViewHolder extends BindingViewHolder<ItemLoadingBinding> {

        public LoadingViewHolder(ItemLoadingBinding binding) {
            super(binding);
        }
    }

    class LoadingItem {

    }

    class LinksAdapter extends SortedRecyclerListAdapter<Object> {

        LoadingItem loadingItem = new LoadingItem();

        public LinksAdapter() {
            super(Object.class, new ItemSorter());

            delegatesManager.addDelegate(new LinkAdapterDelegate());
            delegatesManager.addDelegate(new LoadingAdapterDelegate());
        }

        void showLoading(boolean show) {
            if (show) {
                items.add(loadingItem);
            } else {
                items.remove(loadingItem);
            }
        }
    }

    class ItemSorter implements SortedRecyclerListAdapter.Sorter<Object> {

        @Override
        public boolean areItemsTheSame(Object lhs, Object rhs) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(Object lhs, Object rhs) {
            return false;
        }

        @Override
        public int compare(Object lhs, Object rhs) {
            return 0;
        }
    }

    class LinkAdapterDelegate extends AbsSortedListItemAdapterDelegate<MozScapeLink, Object, LinkViewHolder> {

        @Override
        protected boolean isForViewType(@NonNull Object item, SortedList<Object> items, int position) {
            return item instanceof MozScapeLink;
        }

        @NonNull
        @Override
        public LinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
            ItemLinkBinding binding = ItemLinkBinding.inflate(getLayoutInflater(), parent, false);
            return new LinkViewHolder(binding);
        }

        @Override
        protected void onBindViewHolder(@NonNull MozScapeLink item, @NonNull LinkViewHolder viewHolder) {
            MozScapeLinkViewModel viewModel = new MozScapeLinkViewModel(item, LinksActivity.this);
            viewHolder.binding.setViewModel(viewModel);
        }
    }

    class LoadingAdapterDelegate extends AbsSortedListItemAdapterDelegate<LoadingItem, Object, LoadingViewHolder> {

        @Override
        protected boolean isForViewType(@NonNull Object item, SortedList<Object> items, int position) {
            return item instanceof LoadingItem;
        }

        @NonNull
        @Override
        public LoadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
            ItemLoadingBinding binding = ItemLoadingBinding.inflate(getLayoutInflater(), parent, false);
            return new LoadingViewHolder(binding);
        }

        @Override
        protected void onBindViewHolder(@NonNull LoadingItem item, @NonNull LoadingViewHolder viewHolder) {

        }
    }

}
