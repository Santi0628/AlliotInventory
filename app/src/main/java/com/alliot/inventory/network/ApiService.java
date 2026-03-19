package com.alliot.inventory.network;

import com.alliot.inventory.model.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("items")
    Call<ItemResponse> getItems(
            @Query("pp") int perPage,
            @Query("p") int page
    );
}
