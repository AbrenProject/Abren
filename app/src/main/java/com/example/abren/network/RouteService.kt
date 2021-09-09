package com.example.abren.network

import com.example.abren.models.Location
import com.example.abren.models.Route
import com.example.abren.models.User
import retrofit2.Call
import retrofit2.http.*


interface RouteService {

   // Outputs: List of all route JSON objects
    @GET("/api/routes")
    fun fetchAllRoutes(): Call<List<Route>>

    //Output: Route JSON object
    @GET("/api/routes/{id}")
    fun fetchRoute(_id: String): Call<Route>

    @POST("api/routes/")
    fun addRoute(_id: String,startingLocation: Location,waypointLocations: ArrayList<Location>,
                 destinationLocation: Location) : Call <Route>

    @DELETE("/api/routes/{id}")
    fun deleteRoute(_id: String) : Call <Route>

    @PUT("/api/routes/{id}")
    fun updateRoute(_id: String, startingLocation: Location,waypointLocations: ArrayList<Location>,
                    destinationLocation: Location) : Call <Route>


}