package com.example.abren.network

import com.example.abren.models.Location
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationsService {
    @GET("search?limit=10&format=json&viewbox=38.6593724,8.836611977,38.91749263,9.091672497&bounded=1")
    fun fetchAllLocations(@Query("q") query: String): Call<List<Location>>
}