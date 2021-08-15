package com.example.abren.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Location
import com.example.abren.repository.LocationRepository

class LocationViewModel(application: Application): AndroidViewModel(application) {
    private var locationRepository: LocationRepository?=null
    var locationListLiveData : LiveData<List<Location>>?=null


    init {
        locationRepository = LocationRepository()
        locationListLiveData = MutableLiveData()
    }

    fun fetchAllLocations(query: String){
        locationListLiveData = locationRepository?.fetchAllLocations(query)
    }
}