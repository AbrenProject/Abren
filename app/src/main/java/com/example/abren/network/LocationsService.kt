package com.example.abren.network

import com.example.abren.models.Location
import retrofit2.Call
import retrofit2.http.GET

interface LocationsService {
    @GET("search?q=Goro&limit=10&format=json")
    fun fetchAllLocations(): Call<List<Location>>
}