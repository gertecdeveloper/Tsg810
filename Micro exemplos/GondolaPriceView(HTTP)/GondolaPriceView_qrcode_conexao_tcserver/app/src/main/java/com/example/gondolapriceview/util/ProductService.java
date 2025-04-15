package com.example.gondolapriceview.util;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductService {
    @GET("/barcode")
    Call<String> getProductInfo(
            @Query("param") String barcode
    );
}
