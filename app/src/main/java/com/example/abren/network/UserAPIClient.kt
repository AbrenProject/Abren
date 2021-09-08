package com.example.abren.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class UserAPIClient {

    companion object{
        private var retrofit: Retrofit?=null
        fun getApiClient():Retrofit{
            val gson = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setLenient()
                .create()
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100,TimeUnit.SECONDS)
                .addInterceptor{ chain ->
                    val request: Request =
                        chain.request().newBuilder().addHeader("Accept","multipart/form-data").build()
                        chain.proceed(request)
                }
                .build()
            if(retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl("https://abren-project.herokuapp.com/api/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!
        }
    }



}