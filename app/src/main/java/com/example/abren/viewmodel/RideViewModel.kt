package com.example.abren.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.abren.models.Location
import com.example.abren.models.Ride
import com.example.abren.models.Route
import com.example.abren.repository.RideRepository
import com.example.abren.repository.RouteRepository
import com.example.abren.responses.RidesResponse

class RideViewModel (savedStateHandle: SavedStateHandle) : ViewModel()  {
    private var rideRepository: RideRepository? = null

    var nearbyRidesLiveData: MutableLiveData<RidesResponse?>? = null
    var currentNearby: MutableLiveData<Int>? = null
    var currentRequested: MutableLiveData<Int>? = null

    init {
        rideRepository = RideRepository()
        nearbyRidesLiveData = MutableLiveData()
        currentNearby = MutableLiveData()
        currentNearby?.value = 0
        currentRequested = MutableLiveData()
        currentRequested?.value = 0
    }

    fun fetchNearbyRides(requestId: String, location: Location, context: Context) {
        nearbyRidesLiveData = rideRepository?.fetchNearbyRides(requestId, location, context)
    }

    fun setNextNearby() {
        currentNearby?.value = currentNearby?.value?.plus(1)
    }

    fun setPrevNearby() {
        currentNearby?.value = currentNearby?.value?.minus(1)
    }

    fun setNextRequested() {
        currentRequested?.value = currentRequested?.value?.plus(1)
    }

    fun setPrevRequested() {
        currentRequested?.value = currentRequested?.value?.minus(1)
    }
}