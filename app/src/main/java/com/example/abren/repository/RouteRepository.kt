package com.example.abren.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Location
import com.example.abren.models.Route
import com.example.abren.network.LocationAPIClient
import com.example.abren.network.LocationService
import com.example.abren.network.RouteAPIClient
import com.example.abren.network.RouteService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteRepository {

    private var routesService : RouteService ? =null

    init {
        routesService = RouteAPIClient.getApiClient().create(RouteService::class.java)
    }

    fun fetchAllRoutes(): LiveData<List<Route>> {
        val data = MutableLiveData<List<Route>>()

        routesService?.fetchAllRoutes()?.enqueue(object : Callback<List<Route>> {

            override fun onFailure(call: Call<List<Route>>, t: Throwable) {
                data.value = ArrayList()
            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<List<Route>>,
                response: Response<List<Route>>
            ) {

                val res = response.body()
                if (response.code() == 200 && res!=null){
                    data.value = res
                }else{
                    data.value = ArrayList()
                }
            }

//            override fun onFailure(call: Call<List<Route>>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
        })

        return data
    }
    fun fetchRoute(_id : String): LiveData<Route> {
        val data = MutableLiveData<Route>()

        routesService?.fetchRoute(_id)?.enqueue(object : Callback<Route> {

            override fun onFailure(call: Call<Route>, t: Throwable) {

              //  data.value = ArrayList()
                Log.i("RETROFIT", t.message + "")
            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<Route>,
                response: Response<Route>
            ) {

                val res = response.body()
                if (response.code() == 200 && res!=null){
                    data.value = res
                }else{
                    //data.value = Route()
                }
            }

//            override fun onFailure(call: Call<List<Route>>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
        })

        return data
    }


    fun addRoute(_id: String,startingLocation: Location,waypointLocations: ArrayList<Location>,
                 destinationLocation: Location): LiveData<Route> {
        val data = MutableLiveData<Route>()

        routesService?.addRoute(_id,startingLocation,waypointLocations,
        destinationLocation)?.enqueue(object : Callback<Route> {

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<Route>,
                response: Response<Route>
            ) {

                val res = response.body()
                if (response.code() == 200 && res!=null){
                    data.value = res
                }else{
                   // data.value = ArrayList();
                }
            }

            override fun onFailure(call: Call<Route>, t: Throwable) {
                TODO("Not yet implemented")
              //  data.value = Route();
            }
        })

        return data
    }



    fun deleteRoute(_id: String): LiveData<Route> {
        val data = MutableLiveData<Route>()

        routesService?.deleteRoute(_id)?.enqueue(object : Callback<Route> {

            override fun onFailure(call: Call<Route>, t: Throwable) {

                // data.value = Route()
                Log.i("RETROFIT", t.message + "")
            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<Route>,
                response: Response<Route>
            ) {

                val res = response.body()
                if (response.code() == 200 && res!=null){
                    data.value = res
                }else{
                    // data.value = ArrayList();
                }
            }


        })

        return data
    }

    fun updateRoute(_id: String,startingLocation: Location,waypointLocations: ArrayList<Location>,
                    destinationLocation: Location): LiveData<Route> {
        val data = MutableLiveData<Route>()

        routesService?.updateRoute(_id,startingLocation,waypointLocations,
            destinationLocation)?.enqueue(object : Callback<Route> {

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<Route>,
                response: Response<Route>
            ) {

                val res = response.body()
                if (response.code() == 200 && res!=null){
                    data.value = res
                }else{
                    // data.value = ArrayList();
                }
            }

            override fun onFailure(call: Call<Route>, t: Throwable) {
                TODO("Not yet implemented")
                //  data.value = Route();
            }
        })

        return data
    }



}