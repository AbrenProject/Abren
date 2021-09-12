package com.example.abren.network

import android.util.Log
import com.example.abren.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import java.util.logging.Level
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit

object RetrofitClient {

    const val BASE_URL = "https://abren-project.herokuapp.com/api/"

    val retrofitClient: Retrofit.Builder by lazy {

        val gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setLenient()
            .create()
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request: Request =
//                    chain.request().newBuilder().addHeader("Accept", "multipart/form-data").build()
                    chain.request().newBuilder().addHeader("Content-Type", "multipart/form-data").build()
                Log.d("request header", request.toString())
                chain.proceed(request)
            }
            .build()


            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
    }

        val apiInterface: ApiInterfaceService by lazy {
            retrofitClient
                .build()
                .create(ApiInterfaceService::class.java)
        }
}



