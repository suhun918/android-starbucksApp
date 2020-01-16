package com.cos.mystarbucks.service;

import com.cos.mystarbucks.model.PointCardDTO;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface CardService {
    //http://192.168.0.50:8080/android/card
    @GET("/android/card")
    Call<PointCardDTO> repoContributors(

    );

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.50:8080/")
                //converter가 자동으로 변환 해준다.
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
