package com.example.abren.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//
interface UserService {
    @GET("auth/login/")
    fun login(@Query("phoneNumber") phoneNumber: String, @Query("password") password: String)
}