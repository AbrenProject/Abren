package com.example.abren.network

import android.view.Menu
import com.example.abren.models.MenuItem
import com.example.abren.models.MenuItemWrapper
import com.example.abren.models.VehicleInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VehicleInformationService {
    @GET("/ws/rest/vehicle/menu/year")
    fun fetchYears(): Call<VehicleInformation>

    @GET("/ws/rest/vehicle/menu/make")
    fun fetchMakes(@Query("year") year: String): Call<VehicleInformation>

    @GET("/ws/rest/vehicle/menu/model")
    fun fetchModels(@Query("year") year: String, @Query("make") make: String): Call<VehicleInformation>

    @GET("/ws/rest/vehicle/menu/options")
    fun fetchOptions(@Query("year") year: String, @Query("make") make: String, @Query("model") model: String): Call<VehicleInformation>

    @GET("/ws/rest/vehicle/menu/options")
    fun fetchOptionsOne(@Query("year") year: String, @Query("make") make: String, @Query("model") model: String): Call<MenuItemWrapper>

    @GET("/ws/rest/vehicle/{value}")
    fun fetchVehicle(@Path("value") value: String): Call<VehicleInformation>
}
