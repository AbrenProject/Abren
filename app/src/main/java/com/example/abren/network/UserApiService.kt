package com.example.abren.network

import com.example.abren.models.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface UserApiService {

    @Headers("Content-Type:multipart/form-data")
    @POST("api/auth/signup")
    fun registerUser(@Body info: User):
            retrofit2.Call<ResponseBody>

    @Headers("Content-Type:multipart/form-data")
    @POST("user")
    fun registerUser1(@Body user: User):
            retrofit2.Call<ResponseBody>


    @Multipart
    @POST("upload")
    fun upload(
        @Part("description") description: RequestBody?,
        @Part file: Part?
    ): Call<ResponseBody?>?


    @Headers("Content-Type:multipart/form-data")
    @Multipart
    @POST("/api/auth/signup")
    fun addUser(
        @Part("file\"; filename=\"pp.png\" ") file: RequestBody?,
        @Part("FirstName") fname: RequestBody?,
        @Part("Id") id: RequestBody?
    ): Call<User?>?
}