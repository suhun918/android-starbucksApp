package com.cos.mystarbucks.service;

import com.cos.mystarbucks.model.MyPageDTO;
import com.cos.mystarbucks.util.Localhost;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface MyPageService {
    @GET("/android/mypage")
    Call<MyPageDTO> repoContributors(@Header("Cookie") String cookie);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Localhost.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
