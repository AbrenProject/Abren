package com.example.abren.network

import com.example.abren.models.User
import com.example.abren.models.VehicleInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface UserProfileService {


    @GET("/api/users/profile")
    fun fetchAllProfiles(): Call<User>


    @PUT("/api/users/profile")
    fun updateProfile() : Call <User>


    @POST("/api/users/rate{id}")
    fun addProfile() : Call <User>



}