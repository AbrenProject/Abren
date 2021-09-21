package com.example.abren.network

import com.example.abren.models.Route
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RouteService {
    @POST("routes")
    fun createRoute(@Body route: Route, @Header("Authorization") token: String): Call<Route>
}