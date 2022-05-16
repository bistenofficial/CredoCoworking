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
    private static final String BASE_URL = "http://192.168.0.108:8080/api/v1/";

    private ApiManager() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
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

    public void signInAgent(Agent agent, Callback<ResponseBody> callback) {
        Call<ResponseBody> agentCall = service.signIn(agent);
        agentCall.enqueue(callback);
    }
    public void getAgentData(String phone, Callback<Agent> callback)
    {
        Call<Agent> agentCall = service.getAgentData(phone);
        agentCall.enqueue(callback);
    }
    public void updateAgentData(Agent agent,Callback<ResponseBody> callback)
    {
        Call<ResponseBody> agentCall = service.agentUpdate(agent);
        agentCall.enqueue(callback);
    }
}
