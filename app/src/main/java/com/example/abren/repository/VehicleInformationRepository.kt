package com.example.abren.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.MenuItem
import com.example.abren.models.VehicleInformation
import com.example.abren.network.VehicleInformationAPIClient
import com.example.abren.network.VehicleInformationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VehicleInformationRepository {
    private var vehicleInformationService: VehicleInformationService?=null

    init {
        vehicleInformationService = VehicleInformationAPIClient.getApiClient().create(VehicleInformationService::class.java)
    }

    fun fetchYears(): LiveData<ArrayList<MenuItem>> {
        Log.i("RETROFIT", "Calling in Repository")
        val data = MutableLiveData<ArrayList<MenuItem>>()

        vehicleInformationService?.fetchYears()?.enqueue(object : Callback<VehicleInformation> {

            override fun onFailure(call: Call<VehicleInformation>, t: Throwable) {
                data.value = ArrayList()
                Log.i("RETROFIT", t.message + "")

            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<VehicleInformation>,
                response: Response<VehicleInformation>
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

    fun fetchMakes(year: String): LiveData<ArrayList<MenuItem>> {
        Log.i("RETROFIT", "Calling in Repository")
        val data = MutableLiveData<ArrayList<MenuItem>>()

        vehicleInformationService?.fetchMakes(year)?.enqueue(object : Callback<VehicleInformation> {

            override fun onFailure(call: Call<VehicleInformation>, t: Throwable) {
                data.value = ArrayList()
                Log.i("RETROFIT", t.message + "")

            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<VehicleInformation>,
                response: Response<VehicleInformation>
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

    fun fetchModels(year: String, make: String): LiveData<ArrayList<MenuItem>> {
        Log.i("RETROFIT", "Calling in Repository")
        val data = MutableLiveData<ArrayList<MenuItem>>()

        vehicleInformationService?.fetchModels(year, make)?.enqueue(object : Callback<VehicleInformation> {

            override fun onFailure(call: Call<VehicleInformation>, t: Throwable) {
                data.value = ArrayList()
                Log.i("RETROFIT", t.message + "")

            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<VehicleInformation>,
                response: Response<VehicleInformation>
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