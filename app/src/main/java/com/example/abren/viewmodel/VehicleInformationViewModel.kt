package com.example.abren.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abren.models.MenuItem
import com.example.abren.models.VehicleInformation
import com.example.abren.repository.VehicleInformationRepository

class VehicleInformationViewModel: ViewModel() {
    private var vehicleInformationRepository: VehicleInformationRepository?=null
    var vehicleYearListLiveData : LiveData<ArrayList<MenuItem>>?=null
    var vehicleMakeListLiveData : LiveData<ArrayList<MenuItem>>?=null
    var vehicleModelListLiveData : LiveData<ArrayList<MenuItem>>?=null


    init {
        vehicleInformationRepository = VehicleInformationRepository()
        vehicleYearListLiveData = MutableLiveData()
    }

    fun fetchYears(){
        Log.i("RETROFIT", "Calling in View Model")
        vehicleYearListLiveData = vehicleInformationRepository?.fetchYears()
    }

    fun fetchMakes(year: String){
        Log.i("RETROFIT", "Calling in View Model")
        vehicleMakeListLiveData = vehicleInformationRepository?.fetchMakes(year)
    }

    fun fetchModels(year: String, make: String){
        Log.i("RETROFIT", "Calling in View Model")
        vehicleModelListLiveData = vehicleInformationRepository?.fetchModels(year, make)
    }
}