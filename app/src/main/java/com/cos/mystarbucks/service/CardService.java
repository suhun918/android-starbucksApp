package com.cos.mystarbucks.service;

import com.cos.mystarbucks.model.PointCardDTO;
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

public interface CardService {
    //http://192.168.0.50:8080/android/card
    @GET("/android/card")
    Call<PointCardDTO> repoContributors();

    @FormUrlEncoded
    @POST("/android/enrollment")
    Call<ResponseBody> enrollment(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST("/android/existCard")
    Call<ResponseBody> existCard(@FieldMap Map<String, String> body);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Localhost.URL)
                //converter가 자동으로 변환 해준다.
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
