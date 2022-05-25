package com.example.openroom.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService
{
    @POST("signup")
    fun createAgent(@Body agent: Agent?): Call<ResponseBody?>?

    @POST("signing")
    fun signIn(@Body agent: Agent?): Call<ResponseBody?>?

    @GET("get-userdata?")
    fun getAgentData(@Query("phone") phone: String?): Call<Agent?>?

    @PUT("update")
    fun agentUpdate(@Body agent: Agent?): Call<ResponseBody?>?
}