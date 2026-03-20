package com.alliot.inventory.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alliot.inventory.model.ItemResponse;
import com.alliot.inventory.network.ApiService;
import com.alliot.inventory.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRepository {

    private static final int ITEMS_PER_PAGE = 15;

    private final ApiService apiService;

    public ItemRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public LiveData<Resource<ItemResponse>> getItems(int page) {
        MutableLiveData<Resource<ItemResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.getItems(ITEMS_PER_PAGE, page).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ItemResponse> call,
                                   @NonNull Response<ItemResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Resource.success(response.body()));
                } else {
                    result.postValue(Resource.error(
                            ErrorType.fromHttpCode(response.code())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ItemResponse> call, @NonNull Throwable t) {
                result.postValue(Resource.error(
                        ErrorType.fromThrowable(t)));
            }
        });

        return result;
    }
}
