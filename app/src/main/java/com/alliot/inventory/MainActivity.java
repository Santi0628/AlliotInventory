package com.alliot.inventory;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alliot.inventory.adapter.ItemAdapter;
import com.alliot.inventory.model.Item;
import com.alliot.inventory.ui.ItemDetailActivity;
import com.alliot.inventory.viewmodel.ItemListViewModel;

public class MainActivity extends AppCompatActivity {

    private ItemListViewModel viewModel;
    private ItemAdapter adapter;
    private RecyclerView rvItems;
    private LinearLayout loadingLayout;
    private View loadingMoreCard;
    private LinearLayout errorLayout;
    private TextView tvError;
    private View emptyLayout;
    private TextView tvItemCount;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupSearch();
        setupViewModel();

        if (savedInstanceState == null) {
            viewModel.loadItems();
        }
    }

    private void initViews() {
        rvItems = findViewById(R.id.rvItems);
        loadingLayout = findViewById(R.id.loadingLayout);
        loadingMoreCard = findViewById(R.id.loadingMoreCard);
        errorLayout = findViewById(R.id.errorLayout);
        tvError = findViewById(R.id.tvError);
        emptyLayout = findViewById(R.id.emptyLayout);
        tvItemCount = findViewById(R.id.tvItemCount);
        searchView = findViewById(R.id.searchView);
        findViewById(R.id.btnRetry).setOnClickListener(v -> viewModel.loadItems());
    }

    private void setupRecyclerView() {
        int spanCount = calculateSpanCount();

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(false);

        adapter = new ItemAdapter();
        rvItems.setAdapter(adapter);

        adapter.setOnItemClickListener(this::navigateToDetail);

        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy <= 0) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (!viewModel.isCurrentlyLoading() && !viewModel.isLastPage()) {
                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount - 5) {
                        viewModel.loadNextPage();
                    }
                }
            }
        });
    }

    private int calculateSpanCount() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float screenWidthDp = metrics.widthPixels / metrics.density;
        int columns = (int) (screenWidthDp / 180);
        int minColumns = screenWidthDp >= 600 ? 3 : 2;
        return Math.max(minColumns, columns);
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setSearchQuery(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            viewModel.setSearchQuery("");
            return false;
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(ItemListViewModel.class);

        viewModel.getFilteredItems().observe(this, items -> {
            adapter.setItems(items);

            if (items != null && !items.isEmpty()) {
                tvItemCount.setVisibility(View.VISIBLE);
                tvItemCount.setText(String.format(
                        getString(R.string.item_count_format),
                        viewModel.getLoadedItemCount(),
                        viewModel.getTotalItems()
                ));
            } else {
                tvItemCount.setVisibility(View.GONE);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            loadingLayout.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                rvItems.setVisibility(View.GONE);
                errorLayout.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
            }
        });

        viewModel.getIsLoadingMore().observe(this, isLoadingMore ->
            loadingMoreCard.setVisibility(isLoadingMore ? View.VISIBLE : View.GONE)
        );

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                if (viewModel.getLoadedItemCount() == 0) {
                    errorLayout.setVisibility(View.VISIBLE);
                    rvItems.setVisibility(View.GONE);
                    tvError.setText(error);
                } else {
                    errorLayout.setVisibility(View.GONE);
                    com.google.android.material.snackbar.Snackbar
                            .make(rvItems, error, com.google.android.material.snackbar.Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, v -> viewModel.loadNextPage())
                            .show();
                }
            } else {
                errorLayout.setVisibility(View.GONE);
            }
        });

        viewModel.getIsEmpty().observe(this, isEmpty -> {
            if (isEmpty && loadingLayout.getVisibility() != View.VISIBLE
                    && errorLayout.getVisibility() != View.VISIBLE) {
                emptyLayout.setVisibility(View.VISIBLE);
                rvItems.setVisibility(View.GONE);
            } else {
                emptyLayout.setVisibility(View.GONE);
                rvItems.setVisibility(View.VISIBLE);
            }
        });
    }

    private void navigateToDetail(Item item, View sharedElement) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.EXTRA_ITEM, item);
        intent.putExtra(ItemDetailActivity.EXTRA_TRANSITION_NAME,
                "item_image_" + item.getId());

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this, sharedElement, "item_image_" + item.getId());
        startActivity(intent, options.toBundle());
    }
}