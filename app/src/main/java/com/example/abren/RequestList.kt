package com.example.abren

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import com.mapbox.mapboxsdk.Mapbox
//import com.mapbox.mapboxsdk.maps.MapView
//import com.mapbox.mapboxsdk.maps.Style


class RequestList: AppCompatActivity (){

//    private var mapView: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        setContentView(R.layout.activity_main)

//        mapView = findViewById(R.id.mapView)
//        mapView?.onCreate(savedInstanceState)
//        mapView?.getMapAsync { mapboxMap ->

//            mapboxMap.setStyle(Style.MAPBOX_STREETS) {

                // Map is set up and the style has loaded. Now you can add data or make other map adjustments


//            }

        }
    }

//}