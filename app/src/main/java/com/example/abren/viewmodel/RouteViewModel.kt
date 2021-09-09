package com.example.abren.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abren.models.Location
import com.example.abren.models.MenuItem
import com.example.abren.models.Route
import com.example.abren.repository.RouteRepository
import com.example.abren.repository.VehicleInformationRepository

class RouteViewModel : ViewModel(){

    private var routeRepository: RouteRepository? = null
    var routeListLiveData : LiveData<List<Route>>?=null
    var routeIdListLiveData: LiveData<Route>? = null
    var routeStartingLocationLiveData: LiveData<Route>? = null

    init {
        routeRepository = RouteRepository()
        routeListLiveData = MutableLiveData()
    }

    fun fetchAllRoutes(){
        Log.i("RETROFIT", "Calling in View Model")
        routeListLiveData = routeRepository?.fetchAllRoutes()
    }

    fun fetchRoute(_id: String){
        Log.i("RETROFIT", "Calling in View Model")
        routeIdListLiveData = routeRepository?.fetchRoute(_id)
    }

//    fun addRoute(_id: String,startingLocation: Location,waypointLocations: ArrayList<Location>,
//                 destinationLocation: Location){
//        Log.i("RETROFIT", "Calling in View Model")
//        routeIdListLiveData = routeRepository?.addRoute(_id)
//        routeStartingLocationLiveData = routeRepository?.addRoute(startingLocation)
//    }

    fun addRoute(_id: String,startingLocation: Location,waypointLocations: ArrayList<Location>,
                 destinationLocation: Location){
        Log.i("RETROFIT", "Calling in View Model")
        routeIdListLiveData = routeRepository?.addRoute(_id,startingLocation,waypointLocations,
            destinationLocation)

    }


    fun deleteRoute(_id: String){
        Log.i("RETROFIT", "Calling in View Model")
        routeIdListLiveData = routeRepository?.deleteRoute(_id)
    }

    fun updateRoute(_id: String,startingLocation: Location,waypointLocations: ArrayList<Location>,
                    destinationLocation: Location){
        Log.i("RETROFIT", "Calling in View Model")
        routeIdListLiveData = routeRepository?.updateRoute(_id,startingLocation,waypointLocations,
            destinationLocation)
    }






}