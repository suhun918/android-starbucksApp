package com.cos.mystarbucks.service;

import com.cos.mystarbucks.model.BoardDTO;
import com.cos.mystarbucks.util.Localhost;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WhatsNewService {
    //http://192.168.0.50:8080/android/whatsnew
    @GET("/android/whatsnew")
    Call<BoardDTO> repoContributors();

    @FormUrlEncoded
    @POST("/android/whatsnew/next")
    Call<BoardDTO> nextBoard(@Field("position") int pos);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Localhost.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
