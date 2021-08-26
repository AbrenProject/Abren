package com.example.abren.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.abren.models.MenuItem;
import com.example.abren.network.RouteAPIClient;
import com.example.abren.network.RouteService;

import java.util.ArrayList;

public class RouteRepository {

    private var RouteService: com.example.abren.network.RouteService?=null

    init {
      RouteService = RouteAPIClient.getApiClient().create(RouteService::class.java)
    }

    fun fetchRoutes(): LiveData<ArrayList<MenuItem>> {
        Log.i("RETROFIT", "Calling in Repository")
        val data = MutableLiveData<ArrayList<MenuItem>>()

        RouteService?.fetchRoutes()?.enqueue(object : Callback<Route> {

            override fun onFailure(call: Call<Route>, t: Throwable) {
                data.value = ArrayList()
                Log.i("RETROFIT", t.message + "")

            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                    call: Call<Route>,
            response: Response<Route>
            ) {

                val res = response.body()
                Log.i("RETROFIT", response.toString())
                if (response.code() == 200 && res!=null){
                    data.value = res.menuItem
                }else{
                    data.value = ArrayList()
                }
            }
        })
        return data
    }

    fun fetchId(_id: String):LiveData<ArrayList<MenuItem>>

    {
        Log.i("RETROFIT", "Calling in Repository")
        val data = MutableLiveData<ArrayList<MenuItem>>()

        RouteService?.fetchID(_Id)?.enqueue(object : Callback<Route> {

            override fun onFailure(call: Call<Route>, t: Throwable) {
                data.value = ArrayList()
                Log.i("RETROFIT", t.message + "")

            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                    call: Call<Route>,
            response: Response<Route>
            ) {

                val res = response.body()
                Log.i("RETROFIT", response.toString())
                if (response.code() == 200 && res!=null){
                    data.value = res.menuItem
                }else{
                    data.value = ArrayList()
                }
            }
        })
        return data
    }



}
