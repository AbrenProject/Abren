package com.example.abren.network

import com.example.abren.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterfaceService {

    @POST("auth/signup")
    @Multipart
    fun registerUser(
        @Part profilePicture: MultipartBody.Part,
        @Part idCardPicture: MultipartBody.Part,
        @Part idCardBackPicture: MultipartBody.Part,
        @Part("phoneNumber") phoneNumber:RequestBody,
        @Part("emergencyPhoneNumber") emergencyPhoneNumber: RequestBody,
        @Part("role") role:RequestBody,
        @Part("password") password:RequestBody
    ): Call<ResponseBody>

    @POST("auth/signup")
    @Headers("Content-Type:multipart/form-data")
    @FormUrlEncoded
    fun sendUser(@Field("user") user:User):Call<ResponseBody>

    @GET("auth/signup")
    fun response(@Path("user") user:User):Call<ResponseBody>



//    @Headers("Content-Type:multipart/form-data")
//    @Multipart
//    @POST("/api/auth/signup")
//    fun addUser(
//        @Part("file\"; filename=\"pp.png\" ") file: RequestBody?,
//        @Part("FirstName") fname: RequestBody?,
//        @Part("Id") id: RequestBody?
//    ): Call<User?>?
}