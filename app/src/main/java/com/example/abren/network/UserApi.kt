package com.example.abren.network

import com.example.abren.models.User
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApi {

    @Headers("Content-Type:multipart/form-data")
    @POST("user")
    fun registerUser(
        @Body info: User
    ): retrofit2.Call<ResponseBody>
}