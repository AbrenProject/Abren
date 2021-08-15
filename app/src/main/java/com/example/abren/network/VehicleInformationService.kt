package com.example.abren.network

import com.example.abren.models.VehicleInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VehicleInformationService {
    @GET("/ws/rest/vehicle/menu/year")
    fun fetchYears(): Call<VehicleInformation>

    @GET("/ws/rest/vehicle/menu/make")
    fun fetchMakes(@Query("year") year: String): Call<VehicleInformation>

    @GET("/ws/rest/vehicle/menu/model")
    fun fetchModels(@Query("year") year: String, @Query("make") make: String): Call<VehicleInformation>
}