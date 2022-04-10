package com.example.openroom.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService
{
    @POST("signup")
    Call<ResponseBody> createAgent(@Body Agent agent);
}
