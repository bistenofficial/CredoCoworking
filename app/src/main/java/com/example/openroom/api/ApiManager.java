package com.example.openroom.api;

import com.example.openroom.model.AgentModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    private static ApiService service;
    private static final Server server = new Server();
    private static ApiManager apiManager;
    private static final String BASE_URL = server.getServer();

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

    public void createAgent(AgentModel agentModel, Callback<ResponseBody> callback) {
        Call<ResponseBody> agentCall = service.createAgent(agentModel);
        agentCall.enqueue(callback);
    }

    public void signInAgent(AgentModel agentModel, Callback<ResponseBody> callback) {
        Call<ResponseBody> agentCall = service.signIn(agentModel);
        agentCall.enqueue(callback);
    }
    public void getAgentData(String phone, Callback<AgentModel> callback)
    {
        Call<AgentModel> agentCall = service.getAgentData(phone);
        agentCall.enqueue(callback);
    }
    public void updateAgentData(AgentModel agentModel, Callback<ResponseBody> callback)
    {
        Call<ResponseBody> agentCall = service.agentUpdate(agentModel);
        agentCall.enqueue(callback);
    }
}
