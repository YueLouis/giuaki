package com.example.eatandorder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    Call<LoginResponse> login(@Field("username") String username,
                              @Field("password") String password);
    @POST("auth/forgot-password")
    Call<LoginResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("auth/reset-password")
    Call<LoginResponse> resetPassword(@Body ResetPasswordRequest request);

}


