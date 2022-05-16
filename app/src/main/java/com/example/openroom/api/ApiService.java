package com.example.openroom.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("signup")
    Call<ResponseBody> createAgent(@Body Agent agent);

    @POST("signing")
    Call<ResponseBody> signIn(@Body Agent agent);

    @GET("get-userdata?")
    Call<Agent> getAgentData(@Query("phone") String phone);

    @PUT("update")
    Call<ResponseBody> agentUpdate(@Body Agent agent);
}
