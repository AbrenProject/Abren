package com.example.abren.network

import com.example.abren.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterfaceService {

    @POST("auth/signup")
    @Headers("Content-Type:multipart/form-data")
    @FormUrlEncoded
    fun registerUser(
        @Field("profilePicture") profileImage: MultipartBody.Part,
        @Field("idCardPicture") idCardImage: MultipartBody.Part,
        @Field("idCardBackPicture") idCardBackImage: MultipartBody.Part,
        @Field("phoneNumber") phoneNumber:String,
        @Field("emergencyPhoneNumber") emergencyPhoneNumber: String,
        @Field("role") role:String,
        @Field("password") password:String
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