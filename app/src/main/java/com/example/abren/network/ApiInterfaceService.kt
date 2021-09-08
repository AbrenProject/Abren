package com.example.abren.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterfaceService {

    @POST("auth/signup")
    @FormUrlEncoded
    @Multipart
    fun registerUser(
        @Field("profilePicture") profileImage: MultipartBody.Part,
        @Field("idCardPicture") idCardImage: MultipartBody.Part,
        @Field("idCardBackPicture") idCardBackImage: MultipartBody.Part,
        @Field("phoneNumber") phoneNumber:RequestBody,
        @Field("emergencyPhoneNumber") emergencyPhoneNumber: RequestBody,
        @Field("role") role:RequestBody,
        @Field("password") password:String

    ): Call<ResponseBody>

    @POST("auth/signup")
    @FormUrlEncoded
    fun sendUser(@FieldMap params:Map<String,String>):Call<ResponseBody>



//    @Headers("Content-Type:multipart/form-data")
//    @Multipart
//    @POST("/api/auth/signup")
//    fun addUser(
//        @Part("file\"; filename=\"pp.png\" ") file: RequestBody?,
//        @Part("FirstName") fname: RequestBody?,
//        @Part("Id") id: RequestBody?
//    ): Call<User?>?
}