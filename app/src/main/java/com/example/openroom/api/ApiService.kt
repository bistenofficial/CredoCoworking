package com.example.openroom.api

import com.example.openroom.model.AgentModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService
{
    @POST("signup")
    fun createAgent(@Body agentModel: AgentModel?): Call<ResponseBody?>?

    @POST("signing")
    fun signIn(@Body agentModel: AgentModel?): Call<ResponseBody?>?

    @GET("get-userdata?")
    fun getAgentData(@Query("phone") phone: String?): Call<AgentModel?>?

    @PUT("update")
    fun agentUpdate(@Body agentModel: AgentModel?): Call<ResponseBody?>?
}