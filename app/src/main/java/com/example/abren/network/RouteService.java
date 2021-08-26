package com.example.abren.network;

import com.example.abren.models.Route;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface RouteService {

    @GET("/api/routes)
         fun fetchRoutes(): Call<Route>

  //  @GET("/api/routes/{id})
   // fun fetchID(@Query("_id") id: String): Call<Route>



  //  @POST("/api/routes")
//    fun postRoutes(id) : Call<Route>

  //  @DELETE("/api/routes/{id}")



}
