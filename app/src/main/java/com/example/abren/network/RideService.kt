package com.example.abren.network

import com.example.abren.models.Location
import com.example.abren.models.Request
import com.example.abren.models.Ride
import com.example.abren.responses.RidesResponse
import retrofit2.Call
import retrofit2.http.*

interface RideService {
    @POST("rides")
    fun createRide(@Body ride: Ride, @Header("Authorization") token: String): Call<Ride>

    @POST("rides/nearby/{id}")
    fun fetchNearbyRides(
        @Path("id") requestId: String,
        @Body location: Location,
        @Header("Authorization") token: String
    ): Call<RidesResponse>

}