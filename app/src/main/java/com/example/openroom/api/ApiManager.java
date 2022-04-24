package com.example.openroom.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static ApiService service;
    private static ApiManager apiManager;
    private static final String BASE_URL = "http://192.168.0.103:8080/api/v1/auth/";
    private Retrofit retrofit;

    private ApiManager() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(ApiService.class);
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void createAgent(Agent agent, Callback<ResponseBody> callback) {
        Call<ResponseBody> agentCall = service.createAgent(agent);
        agentCall.enqueue(callback);
    }
}
