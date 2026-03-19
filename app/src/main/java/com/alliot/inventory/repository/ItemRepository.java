package com.alliot.inventory.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alliot.inventory.R;
import com.alliot.inventory.model.ItemResponse;
import com.alliot.inventory.network.ApiService;
import com.alliot.inventory.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRepository {

    private static final int ITEMS_PER_PAGE = 15;

    private final ApiService apiService;
    private final Context context;

    public ItemRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public LiveData<Resource<ItemResponse>> getItems(int page) {
        MutableLiveData<Resource<ItemResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        apiService.getItems(ITEMS_PER_PAGE, page).enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(@NonNull Call<ItemResponse> call,
                                   @NonNull Response<ItemResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(Resource.success(response.body()));
                } else {
                    String errorMsg = parseErrorMessage(response);
                    result.postValue(Resource.error(errorMsg));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ItemResponse> call, @NonNull Throwable t) {
                String errorMsg = parseNetworkError(t);
                result.postValue(Resource.error(errorMsg));
            }
        });

        return result;
    }

    private String parseErrorMessage(Response<?> response) {
        switch (response.code()) {
            case 401:
                return context.getString(R.string.error_unauthorized);
            case 403:
                return context.getString(R.string.error_forbidden);
            case 404:
                return context.getString(R.string.error_not_found);
            case 500:
                return context.getString(R.string.error_server);
            default:
                return context.getString(R.string.error_server_code, response.code());
        }
    }

    private String parseNetworkError(Throwable t) {
        if (t instanceof java.net.UnknownHostException) {
            return context.getString(R.string.error_no_internet);
        } else if (t instanceof java.net.SocketTimeoutException) {
            return context.getString(R.string.error_timeout);
        } else if (t instanceof java.net.ConnectException) {
            return context.getString(R.string.error_connection_failed);
        } else {
            return context.getString(R.string.error_connection_generic, t.getMessage());
        }
    }

    public static class Resource<T> {
        public enum Status { LOADING, SUCCESS, ERROR }

        private final Status status;
        private final T data;
        private final String message;

        private Resource(Status status, T data, String message) {
            this.status = status;
            this.data = data;
            this.message = message;
        }

        public static <T> Resource<T> loading() {
            return new Resource<>(Status.LOADING, null, null);
        }

        public static <T> Resource<T> success(T data) {
            return new Resource<>(Status.SUCCESS, data, null);
        }

        public static <T> Resource<T> error(String message) {
            return new Resource<>(Status.ERROR, null, message);
        }

        public Status getStatus() {
            return status;
        }

        public T getData() {
            return data;
        }

        public String getMessage() {
            return message;
        }
    }
}

