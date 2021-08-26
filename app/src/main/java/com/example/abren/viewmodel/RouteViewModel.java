package com.example.abren.viewmodel;

import android.util.Log;

import com.example.abren.repository.RouteRepository;

public class RouteViewModel {
    private var RouteRepository: com.example.abren.repository.RouteRepository?=null


    init {
        RouteRepository = RouteRepository()

    }

    fun fetchRoutes(){
        Log.i("RETROFIT", "Calling in View Model")
        //routesData= vehicleInformationRepository?.fetchYears()
    }


}