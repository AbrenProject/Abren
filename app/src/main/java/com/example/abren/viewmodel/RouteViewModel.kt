package com.example.abren.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.abren.models.Location
import com.example.abren.models.Route
import com.example.abren.repository.RouteRepository

class RouteViewModel (savedStateHandle: SavedStateHandle) : ViewModel()  {
    private var routeRepository: RouteRepository? = null
    private val mutableSelectedRoute = MutableLiveData<Route>()
    val selectedRoute: LiveData<Route> get() = mutableSelectedRoute

    var createdRouteLiveData: MutableLiveData<Route>? = null

    init {
        routeRepository = RouteRepository()
        createdRouteLiveData = MutableLiveData()
    }

    fun createRoute(route: Route, context: Context) {
        createdRouteLiveData = routeRepository?.createRoute(route, context)
    }

    fun setRoute(route: Route) {
        mutableSelectedRoute.value = route
    }

    fun setStartLocation(location: Location) {
        mutableSelectedRoute.value?.startingLocation = location
    }

    fun setDestinationLocation(location: Location) {
        mutableSelectedRoute.value?.destinationLocation = location
    }

    fun addWaypointLocation(location: Location) {
        mutableSelectedRoute.value?.waypointLocations?.add(location)
    }
}