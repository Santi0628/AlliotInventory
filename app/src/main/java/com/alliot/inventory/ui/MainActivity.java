package com.alliot.inventory.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alliot.inventory.R;
import com.alliot.inventory.adapter.ItemAdapter;
import com.alliot.inventory.model.Item;
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

    private boolean isTwoPane;
    private View detailPlaceholder;
    private View detailContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

        detailPlaceholder = findViewById(R.id.detailPlaceholder);
        detailContentView = findViewById(R.id.detailContent);
        isTwoPane = detailPlaceholder != null;
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
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    dismissKeyboardAndClearFocus();
                }
            }

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

        if (isTwoPane) {
            screenWidthDp = screenWidthDp * 2f / 5f;
        }

        int columns = (int) (screenWidthDp / 180);
        int minColumns = screenWidthDp >= 600 ? 3 : 2;
        return Math.max(minColumns, columns);
    }

    private void setupSearch() {
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);

        android.widget.EditText searchEditText =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setFocusable(true);
            searchEditText.setFocusableInTouchMode(true);
            searchEditText.setClickable(true);
            searchEditText.setImeOptions(
                    EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_SEARCH);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.setSearchQuery(query);
                dismissKeyboardAndClearFocus();
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

                boolean hasSearchQuery = viewModel.hasActiveSearch();
                if (hasSearchQuery) {
                    tvItemCount.setText(String.format(
                            getString(R.string.item_count_filtered_format),
                            items.size(),
                            viewModel.getLoadedItemCount()
                    ));

                    if (items.size() < 10 && !viewModel.isLastPage()
                            && !viewModel.isCurrentlyLoading()) {
                        viewModel.loadNextPage();
                    }
                } else {
                    tvItemCount.setText(String.format(
                            getString(R.string.item_count_format),
                            viewModel.getLoadedItemCount(),
                            viewModel.getCurrentPage(),
                            viewModel.getTotalPages()
                    ));
                }
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

        if (isTwoPane) {
            viewModel.getSelectedItem().observe(this, item -> {
                if (item != null) {
                    showDetailInline(item);
                }
            });
        }
    }

    private void navigateToDetail(Item item, View sharedElement) {
        dismissKeyboardAndClearFocus();

        if (isTwoPane) {
            viewModel.setSelectedItem(item);
            return;
        }

        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra(ItemDetailActivity.EXTRA_ITEM, item);
        intent.putExtra(ItemDetailActivity.EXTRA_TRANSITION_NAME,
                "item_image_" + item.getId());

        boolean isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            startActivity(intent);
            overridePendingTransition(R.anim.fade_slide_in, R.anim.fade_slide_out);
        } else if (isSharedElementFullyVisible(sharedElement)) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    this, sharedElement, "item_image_" + item.getId());
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
            overridePendingTransition(R.anim.fade_slide_in, R.anim.fade_slide_out);
        }
    }

    private void showDetailInline(Item item) {
        if (detailPlaceholder != null) {
            detailPlaceholder.setVisibility(View.GONE);
        }
        if (detailContentView != null) {
            detailContentView.setVisibility(View.VISIBLE);

            View btnBack = detailContentView.findViewById(R.id.btnBack);
            if (btnBack != null) {
                btnBack.setVisibility(View.GONE);
            }

            DetailViewHelper.populate(detailContentView, this, item);
        }
    }

    private boolean isSharedElementFullyVisible(View sharedElement) {
        View searchCard = findViewById(R.id.searchCard);
        if (searchCard == null) return true;

        int[] headerLocation = new int[2];
        searchCard.getLocationInWindow(headerLocation);
        int headerBottom = headerLocation[1] + searchCard.getHeight();

        int[] itemLocation = new int[2];
        sharedElement.getLocationInWindow(itemLocation);
        int itemTop = itemLocation[1];

        return itemTop >= headerBottom;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View focused = getCurrentFocus();
            if (focused != null && isKeyboardRelatedView(focused)) {
                Rect searchRect = new Rect();
                View searchCard = findViewById(R.id.searchCard);
                if (searchCard != null) {
                    searchCard.getGlobalVisibleRect(searchRect);
                }
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                if (!searchRect.contains(x, y)) {
                    dismissKeyboardAndClearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean isKeyboardRelatedView(View view) {
        android.widget.EditText searchEditText =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        return view == searchEditText || view == searchView;
    }

    private void dismissKeyboardAndClearFocus() {
        View focused = getCurrentFocus();
        if (focused != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
            }
        }
        searchView.clearFocus();
    }
}
