package com.example.abren.network

import com.example.abren.models.User
import com.example.abren.responses.AuthResponse
import com.example.abren.responses.BadRequestResponse
import retrofit2.Call
import retrofit2.http.*


interface UserService {
    @POST("auth/signup")
    fun registerUser(@Body user: User): Call<AuthResponse>

    @POST("auth/login")
    fun login(@Body user: User): Call<AuthResponse>
}