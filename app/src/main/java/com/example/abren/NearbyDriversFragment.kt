package com.example.abren


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.abren.models.Location
import com.example.abren.viewmodel.RequestViewModel
import com.example.abren.viewmodel.RideViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.fragment_nearby_drivers.*
import androidx.annotation.NonNull

import com.mapbox.mapboxsdk.style.layers.SymbolLayer

import com.mapbox.geojson.FeatureCollection

import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


import android.graphics.BitmapFactory
import android.graphics.Color
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style.OnStyleLoaded
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.mapboxsdk.utils.ColorUtils
import android.R.style

import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions





class NearbyDriversFragment : Fragment(), OnMapReadyCallback, PermissionsListener {

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var permissionManager: PermissionsManager? = null
    private var locationComponent: LocationComponent? = null

    private val SOURCE_ID = "SOURCE_ID"
    private val ICON_ID = "ICON_ID"
    private val LAYER_ID = "LAYER_ID"

    private lateinit var tabAdapter: RiderTabPageAdapter
    private lateinit var viewPager: ViewPager2

    private val requestViewModel: RequestViewModel by activityViewModels()
    private val rideViewModel: RideViewModel by activityViewModels()

    val locationHandler = Handler(Looper.getMainLooper())

    private val apiCallTask = object : Runnable {
        override fun run() {
            makeApiCall()
            locationHandler.postDelayed(this, 5000)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("NearbyFragement", "onCreateView: I am On oncreate view")
        context?.let {
            Mapbox.getInstance(
                it.applicationContext,
                getString(R.string.mapbox_access_token)
            )
        }
        makeApiCall()
        return inflater.inflate(R.layout.fragment_nearby_drivers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
//            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
//                // add data or make other map adjustments
//            }
        tabAdapter = RiderTabPageAdapter(this, 3)
        viewPager = view.findViewById(R.id.viewPager)
        viewPager.adapter = tabAdapter
//
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Nearby"
                1 -> "Requested"
                2 -> "Accepted"
                else -> "Nearby"
            }

            tab.icon =
                when (position) {
                    0 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_nearby)
                    1 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_requested)
                    2 -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_accepted)
                    else -> ContextCompat.getDrawable(requireContext(), R.drawable.ic_nearby)
                }
        }.attach()


        locationHandler.post(apiCallTask)
    }


    override fun onMapReady(mapboxMap: MapboxMap) {

        var symbolManager: SymbolManager? = null
        var geoJsonOptions: GeoJsonOptions? = null

        mapboxMap.setStyle( Style.Builder()
            .fromUri("mapbox://styles/lydiag/ckbey6dzy3mmj1ipgwlpmi7sv")) { style ->
            geoJsonOptions= GeoJsonOptions().withTolerance(0.4f)
            symbolManager = SymbolManager(mapView!!, mapboxMap, style, null, geoJsonOptions)

            symbolManager?.iconAllowOverlap = true
            symbolManager?.iconIgnorePlacement = true

            enableLocationComponent(style)
        }

        mapboxMap.cameraPosition = CameraPosition.Builder()
            .zoom(14.0)
            .build()

        this.mapboxMap = mapboxMap

        rideViewModel.nearbyRidesLiveData?.observe(viewLifecycleOwner, Observer { rides ->
            if (rides != null) {
                rideViewModel.currentNearby?.observe(viewLifecycleOwner, Observer { index ->
                    if (index != null) {
                        if(!rides.nearby.isNullOrEmpty()) {
                            val current = rides.nearby[index]
                            mapboxMap.setStyle(
                                Style.Builder()
                                    .fromUri("mapbox://styles/lydiag/ckbey6dzy3mmj1ipgwlpmi7sv")
                            ) { style ->
                                if(tabLayout.selectedTabPosition == 0){
                                    symbolManager?.deleteAll()
                                    val nearbyOptions = SymbolOptions()
                                        .withLatLng(LatLng(current?.driverLocation?.latitude!!, current.driverLocation?.longitude!!))
                                        .withIconImage("marker")
                                        .withIconColor(ColorUtils.colorToRgbaString(Color.YELLOW))
                                        .withIconSize(0.07f)
                                        .withSymbolSortKey(5.0f)
                                    symbolManager?.create(nearbyOptions)
                                }
                            }
                        }
                    }
                })
            }
        })

        rideViewModel.nearbyRidesLiveData?.observe(viewLifecycleOwner, Observer { rides ->
            if (rides != null) {
                rideViewModel.currentRequested?.observe(viewLifecycleOwner, Observer { index ->
                    if (index != null) {
                        if(!rides.requested.isNullOrEmpty()) {
                            val current = rides.requested[index]
                            mapboxMap.setStyle(
                                Style.Builder()
                                    .fromUri("mapbox://styles/lydiag/ckbey6dzy3mmj1ipgwlpmi7sv")
                            ) { style ->

                                if(tabLayout.selectedTabPosition == 1) {
                                    symbolManager?.deleteAll()
                                    val nearbyOptions = SymbolOptions()
                                        .withLatLng(LatLng(current?.driverLocation?.latitude!!, current.driverLocation?.longitude!!))
                                        .withIconImage("marker")
                                        .withIconColor(ColorUtils.colorToRgbaString(Color.YELLOW))
                                        .withIconSize(0.07f)
                                        .withSymbolSortKey(5.0f)
                                    symbolManager?.create(nearbyOptions)
                                }
                            }
                        }
                    }
                })
            }
        })

        rideViewModel.nearbyRidesLiveData?.observe(viewLifecycleOwner, Observer { rides ->
            if (rides != null) {
                if (rides.accepted != null) {
                    if(tabLayout.selectedTabPosition == 2) {
                        symbolManager?.deleteAll()
                        val nearbyOptions = SymbolOptions()
                            .withLatLng(LatLng(rides.accepted?.driverLocation?.latitude!!, rides.accepted?.driverLocation?.longitude!!))
                            .withIconImage("marker")
                            .withIconColor(ColorUtils.colorToRgbaString(Color.YELLOW))
                            .withIconSize(0.07f)
                            .withSymbolSortKey(5.0f)
                        symbolManager?.create(nearbyOptions)
                    }
                }
            }
        })
    }

    private fun enableLocationComponent(loadedMapStyle: Style?) {
        //check if permission enabled if not request
        context?.let {
            if (PermissionsManager.areLocationPermissionsGranted(it.applicationContext)) {
                // activity the mapboxmap locationComponent to show userlocation
                // adding in locationcomponentOptions is also an optional paramenter
                locationComponent = mapboxMap!!.locationComponent
                context?.let {
                    locationComponent?.activateLocationComponent(
                        it.applicationContext,
                        loadedMapStyle!!
                    )

                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    locationComponent?.isLocationComponentEnabled = true
                    locationComponent?.lastKnownLocation

                    //set the component's camera mode
                    locationComponent?.cameraMode = CameraMode.TRACKING

                }
            } else {
                permissionManager = PermissionsManager(this)
                permissionManager!!.requestLocationPermissions(context as Activity?)
            }
        }

    }

    override fun onExplanationNeeded(p0: MutableList<String>?) {
        Toast.makeText(context, R.string.user_location_permission_explanation, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap!!.style)
        } else {
            Toast.makeText(
                context,
                R.string.user_location_permission_not_granted,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        locationHandler.post(apiCallTask)
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        locationHandler.removeCallbacks(apiCallTask)
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        locationHandler.removeCallbacks(apiCallTask)
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        locationHandler.removeCallbacks(apiCallTask)
        mapView?.onDestroy()
    }

    private fun makeApiCall() {
        Log.d("RIDE", "Making api call")

        requestViewModel.createdRequestLiveData?.observe(viewLifecycleOwner, Observer { request ->
            if (request != null) {
                Log.d("RIDE", "Making api call after observe")

                if(locationComponent == null){
                    rideViewModel.fetchNearbyRides(
                        request._id!!,
                        request.riderLocation!!,
                        requireContext()
                    )
                }else{
                    val currentLocation = locationComponent?.lastKnownLocation
                    val location = Location(name=" ", latitude = currentLocation?.latitude, longitude = currentLocation?.longitude)
                    rideViewModel.fetchNearbyRides(
                        request._id!!,
                        location,
                        requireContext()
                    )
                }
            } else {
                Log.d("RIDE", "Problem in making api call")
                Toast.makeText(this.requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        )
    }


}




