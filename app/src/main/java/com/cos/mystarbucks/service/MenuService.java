package com.cos.mystarbucks.service;

import com.cos.mystarbucks.model.Menu;
import com.cos.mystarbucks.util.Localhost;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface MenuService {
    //http://192.168.0.50:8080/android/menu
    @GET("/android/menu")
    Call<Menu> repoContributors();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Localhost.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
