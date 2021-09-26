package com.example.abren.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.abren.models.Location
import com.example.abren.models.Request
import com.example.abren.models.Ride
import com.example.abren.models.Route
import com.example.abren.repository.RideRepository
import com.example.abren.repository.RouteRepository
import com.example.abren.responses.RidesResponse
import retrofit2.http.Header
import retrofit2.http.Query

class RideViewModel (savedStateHandle: SavedStateHandle) : ViewModel()  {
    private var rideRepository: RideRepository? = null

    private val mutableSelectedRide = MutableLiveData<Ride>()
    val selectedRide: LiveData<Ride> get() = mutableSelectedRide

    var nearbyRidesLiveData: MutableLiveData<RidesResponse?>? = null
    var currentNearby: MutableLiveData<Int>? = null
    var currentRequested: MutableLiveData<Int>? = null
    var currentAcceptedRequest: MutableLiveData<Int>? = null
    var acceptedRidesLiveData: MutableLiveData<RidesResponse?>? = null
    var acceptedRide: MutableLiveData<Ride?>? = null

    var createdRideLiveData: MutableLiveData<Ride?>? = null

    init {
        rideRepository = RideRepository()
        nearbyRidesLiveData = MutableLiveData()
        currentNearby = MutableLiveData()
        currentNearby?.value = 0
        currentRequested = MutableLiveData()
        currentRequested?.value = 0
        createdRideLiveData = MutableLiveData()
        acceptedRidesLiveData = MutableLiveData()
        acceptedRide = MutableLiveData()
        currentAcceptedRequest = MutableLiveData()
    }

    fun setRide(ride: Ride) {
        mutableSelectedRide.value = ride

    }

    fun fetchNearbyRides(requestId: String, location: Location, context: Context) {
        nearbyRidesLiveData = rideRepository?.fetchNearbyRides(requestId, location, context)
    }

    fun createRide(ride: Ride, context: Context) {
        createdRideLiveData = rideRepository?.createRide(ride, context)
    }

    fun setDriverLocation(location: Location) {
        mutableSelectedRide.value?.driverLocation = location
    }

    fun setRouteId(routeId: String) {
        mutableSelectedRide.value?.routeId = routeId
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

    fun acceptRide(rideId: String, requestId: String, context: Context)
    {
        acceptedRide = rideRepository?.acceptRide(rideId, requestId, context)

    }
}