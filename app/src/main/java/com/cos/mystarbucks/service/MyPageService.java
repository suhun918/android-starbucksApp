package com.cos.mystarbucks.service;

import com.cos.mystarbucks.model.MyPageDTO;
import com.cos.mystarbucks.util.Localhost;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MyPageService {
    @GET("/android/mypage")
    Call<MyPageDTO> repoContributors(@Header("Cookie") String cookie);

    @FormUrlEncoded
    @POST("/android/mypage/delete_card")
    Call<ResponseBody> deleteCard(@Header("Cookie") String cookie, @Field("id") int id);

    @FormUrlEncoded
    @POST("/android/mypage/save_beverage")
    Call<ResponseBody> saveBeverage(@Header("Cookie") String cookie, @FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST("/android/mypage/delete_beverage")
    Call<ResponseBody> deleteBeverage(@Header("Cookie") String cookie, @Field("beverageId") int id);

    @FormUrlEncoded
    @POST("/android/mypage/save_coffee")
    Call<ResponseBody> saveCoffee(@Header("Cookie") String cookie, @FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST("/android/mypage/delete_coffee")
    Call<ResponseBody> deleteCoffee(@Header("Cookie") String cookie, @Field("coffeeId") int id);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Localhost.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
