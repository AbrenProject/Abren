 package com.example.abren.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Location
import com.example.abren.network.LocationAPIClient
import com.example.abren.network.LocationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationRepository {
    private var locationsService: LocationService?=null

    init {
        locationsService = LocationAPIClient.getApiClient().create(LocationService::class.java)
    }

    fun fetchAllLocations(query: String): LiveData<List<Location>> {
        val data = MutableLiveData<List<Location>>()

        locationsService?.fetchAllLocations(query)?.enqueue(object : Callback<List<Location>> {

            override fun onFailure(call: Call<List<Location>>, t: Throwable) {
                data.value = ArrayList()
            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<List<Location>>,
                response: Response<List<Location>>
            ) {

                val res = response.body()
                if (response.code() == 200 && res!=null){
                    data.value = res
                }else{
                    data.value = ArrayList()
                }
            }
        })
        return data
    }
}