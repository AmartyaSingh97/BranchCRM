package com.example.branchcrm.network

import com.example.branchcrm.models.MessageModel
import com.example.branchcrm.responseModel.GetMessageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {

    @POST("api/login")
    suspend fun getLoginCredentials(
        @Query("username") email: String,
        @Query("password") password: String
    ): Response<Map<String, String>>

    @GET("api/messages")
    suspend fun getMessages(
        @Header("X-Branch-Auth-Token") authToken: String
    ): Response<GetMessageResponse>

    @POST("api/messages")
    suspend fun sendMessage(
        @Header("X-Branch-Auth-Token") authToken:String,
        @Query("thread_id") threadId: Int,
        @Query("body") message: String
    ): Response<MessageModel>

    @POST("api/reset")
    suspend fun resetApi(@Header("X-Branch-Auth-Token") authToken:String)

 }