package com.example.abren.network;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RouteAPIClient {
  companion object{

      val BaseURL = "https:/api.github.com/search/"

              fun getRouteAPIClient():Retrofit{
                    return Retrofit.Builder()
                            .baseURL(BaseURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()


        }
    }
}
