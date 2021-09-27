package com.example.abren

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import com.example.abren.adapter.RouteAdapter
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.viewmodel.RouteViewModel
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.abren.models.Location
import com.example.abren.models.Ride
import com.example.abren.viewmodel.RideViewModel
import com.google.android.gms.location.*
import com.mapbox.geojson.Point
import com.mapbox.turf.TurfMeasurement

private var PERMISSION_ID = 44

class DriverHomeFragment : Fragment() {

    private val routeViewModel: RouteViewModel by activityViewModels()
    private val rideViewModel: RideViewModel by activityViewModels()
    private lateinit var listAdapter: RouteAdapter

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routeViewModel.listRoutes(requireContext())
        return inflater.inflate(R.layout.fragment_driver_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ride = Ride()
        rideViewModel.setRide(ride)

        val mListView = view.findViewById<View>(R.id.list_listView) as ListView

        routeViewModel.createdRouteLiveDataList?.observe(viewLifecycleOwner, Observer { routeList ->

            val views = arrayOfNulls<String>(routeList.size)

            listAdapter = RouteAdapter(requireContext(), 0, routeList)
            listAdapter.addAll(*views)
            Log.d("views = ", views.toString())
            mListView.adapter = listAdapter
        })

        mListView.onItemClickListener = AdapterView.OnItemClickListener(fun(
            _: AdapterView<*>,
            _: View?,
            position: Int,
            _: Long
        ) {
            val selectedRoute = listAdapter.getObject(position)
            rideViewModel.setRouteId(selectedRoute._id!!)
            rideViewModel.selectedRide.observe(viewLifecycleOwner, Observer { ride ->
                rideViewModel.createRide(ride, requireContext())

                rideViewModel.createdRideLiveData?.observe(viewLifecycleOwner, Observer {
                    val destination = selectedRoute.destinationLocation

                    val distance = TurfMeasurement.distance(
                        Point.fromLngLat(ride.driverLocation?.longitude!!,
                            ride.driverLocation?.latitude!!
                        ), Point.fromLngLat(destination?.longitude!!,
                            destination.latitude!!
                        ))

                    rideViewModel.setKm(distance)
                    Log.d("DISTANCE", distance.toString())

                    findNavController().navigate(R.id.action_driverHomeFragment_to_nearbyRidersFragment)
                })
            })

        })

        view.findViewById<Button>(R.id.create_route_button).setOnClickListener {
            findNavController().navigate(R.id.action_driverHomeFragment_to_createRouteFragment)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        Log.d("LOCATION", "In get last location")
        if (checkPermissions()) {
            Log.d("LOCATION", "After check permissions")
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation
                    .addOnCompleteListener { task ->
                        val location: android.location.Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                            Log.d("LOCATION", "After location is null")
                        } else {
                            Log.d("LOCATION", "After location is not null")
                            Toast.makeText(
                                this.requireContext(),
                                "Location Retrieved",
                                Toast.LENGTH_LONG
                            ).show()
                            rideViewModel.setDriverLocation(
                                Location(
                                    name = "",
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                            )
                        }
                    }
            } else {
                Toast.makeText(
                    this.requireContext(),
                    "Please turn on\" + \" your location...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        Log.d("LOCATION", "In request new location")
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1 //TODO: Set expiration date?

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d("LOCATION", "In location result")
            val mLastLocation: android.location.Location = locationResult.lastLocation
            Toast.makeText(
                requireContext(),
                "Location Retrieved",
                Toast.LENGTH_LONG
            ).show()
            rideViewModel.setDriverLocation(
                Location(
                    name = "",
                    latitude = mLastLocation.latitude,
                    longitude = mLastLocation.longitude
                )
            )
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            ContextCompat.getSystemService(requireContext(), LocationManager::class.java)
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        getLastLocation()
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }
}

