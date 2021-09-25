package com.example.abren.network

import com.example.abren.models.Request
import com.example.abren.models.Ride
import retrofit2.Call
import retrofit2.http.*

interface RequestService {
    @POST("requests")
    fun createRequest(@Body request: Request, @Header("Authorization") token: String): Call<Request>

    @PUT("requests/{id}")
    fun sendRequest(
        @Path("id") requestId: String,
        @Query("rideId") rideId: String,
        @Header("Authorization") token: String
    ): Call<Ride>

    @POST("requests/start/{id}")
    fun startRide(
        @Path("id") requestId: String,
        @Query("otp") otp: String,
        @Header("Authorization") token: String
    ): Call<Request>
}