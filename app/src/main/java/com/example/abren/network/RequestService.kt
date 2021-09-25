package com.example.abren.network

import com.example.abren.models.Request
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RequestService {
    @POST("requests")
    fun createRequest(@Body request: Request, @Header("Authorization") token: String): Call<Request>

    @GET("api/requests/current/{id}")
    fun getRequests() {
    }

}


//Inputs:
//Required:
//name: String
//latitude: Double
//longitude: Double

