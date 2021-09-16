package com.example.abren.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Request
import com.example.abren.models.Route
import com.example.abren.network.RequestService
import com.example.abren.network.RetrofitClient
import com.example.abren.network.RouteService
import com.example.abren.responses.BadRequestResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class RouteRepository {
    private var routeService: RouteService?=null
    private lateinit var prefs: SharedPreferences

    init {
        routeService = RetrofitClient.getApiClient().create(RouteService::class.java)
    }

    private val data = MutableLiveData<Route>()

    fun createRoute(route: Route, context: Context): MutableLiveData<Route> {
        Log.i("Route Repo: Create Request", route.toString())

        prefs = context.getSharedPreferences("ABREN", Context.MODE_PRIVATE)

        routeService?.createRoute(route, "Bearer ${prefs.getString("TOKEN", null)}")?.enqueue(object : Callback<Route> {
            override fun onFailure(call: Call<Route>, t: Throwable) {
                Log.d("Route Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<Route>, response: Response<Route>) {
                val gson = Gson()
                if (response.code() == 200) {
                    data.value = response.body()
                    Log.d("Route Repo: On 200:", response.body().toString())
                }else {
                    Log.d("Route Repo: Header = ", response.headers().toString())

                    if(response.code() != 401){
                        val errorResponse = gson.fromJson(response.errorBody()?.string(), BadRequestResponse::class.java)
                        Log.d("Route Repo: Error Body", errorResponse.message)
                    }else{
                        Log.d("Route Repo: Error - ", "Unauthorized")
                    }
                }
            }
        })

        return data
    }
}