package com.cos.mystarbucks.service;

import com.cos.mystarbucks.model.User;
import com.cos.mystarbucks.util.Localhost;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserService {
    @Headers({
            "User-Agent: Android"
    })
    @FormUrlEncoded
    @POST("/user/loginProc")
    Call<User> login(@FieldMap Map<String, String> body);

    @FormUrlEncoded
    @POST("/android/joinProc")
    Call<ResponseBody> join(@FieldMap Map<String, String> body);

    @GET("/test")
    Call<ResponseBody> test(@Header("Cookie") String cookie);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Localhost.URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}