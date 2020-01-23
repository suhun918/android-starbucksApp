package com.cos.mystarbucks.service;


import com.cos.mystarbucks.model.SirenDTO;
import com.cos.mystarbucks.util.Localhost;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SirenService {
//http://192.168.0.50:8080/android/siren
    @GET("/android/siren")
    Call<SirenDTO> repoContributors();

    @FormUrlEncoded
    @POST("/android/order")
    Call<ResponseBody> order(@FieldMap Map<String, String> body);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Localhost.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
