package com.alliot.inventory.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.alliot.inventory.model.Item;
import com.alliot.inventory.model.ItemResponse;
import com.alliot.inventory.repository.ItemRepository;
import com.alliot.inventory.repository.ItemRepository.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the main item list screen.
 */
public class ItemListViewModel extends AndroidViewModel {

    private final ItemRepository repository;
    private final List<Item> allItems = new ArrayList<>();
    private final MutableLiveData<List<Item>> filteredItems = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isLoadingMore = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(false);

    private int currentPage = 0;
    private int totalItems = 0;
    private boolean isLastPage = false;
    private boolean isCurrentlyLoading = false;
    private String currentSearchQuery = "";

    private LiveData<Resource<ItemResponse>> currentLiveData;
    private Observer<Resource<ItemResponse>> currentObserver;

    public ItemListViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
    }

    /** Resets state and starts loading from page 1. */
    public void loadItems() {
        removeCurrentObserver();
        currentPage = 0;
        allItems.clear();
        isLastPage = false;
        totalItems = 0;
        isCurrentlyLoading = false;
        loadNextPage();
    }

    /** Loads the next page of items through the repository. */
    public void loadNextPage() {
        if (isCurrentlyLoading || isLastPage) return;

        int nextPage = currentPage + 1;
        isCurrentlyLoading = true;

        if (currentPage == 0) {
            isLoading.setValue(true);
        } else {
            isLoadingMore.setValue(true);
        }

        removeCurrentObserver();
        currentLiveData = repository.getItems(nextPage);
        currentObserver = resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case LOADING:
                    break;

                case SUCCESS:
                    isCurrentlyLoading = false;
                    isLoading.setValue(false);
                    isLoadingMore.setValue(false);
                    errorMessage.setValue(null);

                    ItemResponse body = resource.getData();
                    if (body != null) {
                        currentPage = body.getPage();
                        totalItems = body.getTotal();
                        isLastPage = !body.hasMorePages();

                        if (body.getData() != null) {
                            allItems.addAll(body.getData());
                        }
                    }
                    applySearchFilter();
                    isEmpty.setValue(allItems.isEmpty());
                    break;

                case ERROR:
                    isCurrentlyLoading = false;
                    isLoading.setValue(false);
                    isLoadingMore.setValue(false);
                    errorMessage.setValue(resource.getMessage());
                    isEmpty.setValue(allItems.isEmpty());
                    break;
            }
        };
        currentLiveData.observeForever(currentObserver);
    }

    public void setSearchQuery(String query) {
        this.currentSearchQuery = query != null ? query.trim() : "";
        applySearchFilter();
    }

    private void applySearchFilter() {
        List<Item> result;
        if (currentSearchQuery.isEmpty()) {
            result = new ArrayList<>(allItems);
        } else {
            String lowerQuery = currentSearchQuery.toLowerCase();
            result = new ArrayList<>();
            for (Item item : allItems) {
                boolean matchName = item.getName() != null
                        && item.getName().toLowerCase().contains(lowerQuery);
                boolean matchSku = item.getInternalSku() != null
                        && item.getInternalSku().toLowerCase().contains(lowerQuery);
                boolean matchExtSku = item.getExternalSku() != null
                        && item.getExternalSku().toLowerCase().contains(lowerQuery);
                boolean matchBarcode = item.getBarcode() != null
                        && item.getBarcode().toLowerCase().contains(lowerQuery);

                if (matchName || matchSku || matchExtSku || matchBarcode) {
                    result.add(item);
                }
            }
        }
        try {
            filteredItems.setValue(result);
        } catch (Exception e) {
            filteredItems.postValue(result);
        }
    }

    /** Removes current observer to avoid duplicate callbacks. */
    private void removeCurrentObserver() {
        if (currentLiveData != null && currentObserver != null) {
            currentLiveData.removeObserver(currentObserver);
        }
        currentLiveData = null;
        currentObserver = null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        removeCurrentObserver();
    }

    // ========== Getters ==========
    public LiveData<List<Item>> getFilteredItems() { return filteredItems; }
    public LiveData<Boolean> getIsLoading()        { return isLoading; }
    public LiveData<Boolean> getIsLoadingMore()    { return isLoadingMore; }
    public LiveData<String> getErrorMessage()      { return errorMessage; }
    public LiveData<Boolean> getIsEmpty()          { return isEmpty; }
    public boolean isLastPage()                    { return isLastPage; }
    public boolean isCurrentlyLoading()            { return isCurrentlyLoading; }
    public int getTotalItems()                     { return totalItems; }
    public int getLoadedItemCount()                { return allItems.size(); }
}
