package com.cos.mystarbucks.service;

import com.cos.mystarbucks.model.MyPageDTO;
import com.cos.mystarbucks.util.Localhost;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MyPageService {
    @GET("/android/mypage")
    Call<MyPageDTO> repoContributors(@Header("Cookie") String cookie);

    @FormUrlEncoded
    @POST("/android/mypage/recharge")
    Call<ResponseBody> recharge(@Header("Cookie") String cookie, @Field("point") int point);

    @FormUrlEncoded
    @POST("/android/mypage/delete_card")
    Call<ResponseBody> deleteCard(@Header("Cookie") String cookie, @Field("id") int id);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Localhost.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
