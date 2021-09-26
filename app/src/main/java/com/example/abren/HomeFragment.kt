package com.example.abren

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abren.adapter.LocationSuggestionAdapter
import com.example.abren.models.Request
import com.example.abren.models.User
import com.example.abren.viewmodel.LocationViewModel
import com.example.abren.viewmodel.RequestViewModel
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import com.example.abren.models.Location as LocationModel


private val TRIGGER_AUTO_COMPLETE = 100
private val AUTO_COMPLETE_DELAY: Long = 300
private var PERMISSION_ID = 44

class HomeFragment : Fragment() {

    private val requestViewModel: RequestViewModel by activityViewModels()
    var mLocation: ArrayList<Location>? = null
//    var mAdapter:LocationAdapter? =null
    private lateinit var destinationLocationSuggestionAdapter: LocationSuggestionAdapter
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var destinationHandler: Handler
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        view?.findViewById<TextView>(R.id.last_traveled_textview)?.setText("this is the text")
//        var mCardView = view?.findViewById<RecyclerView>(R.id.recent_destinations_cardView);
//        var mLayoutManager = LinearLayoutManager(context);
//        mAdapter =LocationSuggestionAdapter(mLocation);
//        mCardView?.setLayoutManager(mLayoutManager);
//        mCardView?.setAdapter(mAdapter);


        destinationLocationSuggestionAdapter = LocationSuggestionAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        getLastLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        val location=sharedPref?.getString(userId,"location")
//        Log.d("location get:", location.toString())
//        view?.findViewById<TextView>(R.id.last_traveled_textview)?.setText("this is the text")
        val userId=User()._id
        val gson = Gson()
        val json: String = sharedPref?.getString(userId, "Place Name").toString()
//        val type: Type = object : TypeToken<ArrayList<Location?>?>() {}.type
//        mLocation = gson.fromJson(json, type)

        if (mLocation == null) {
            mLocation = ArrayList()
        }
        view?.findViewById<TextView>(R.id.destination_name_text_view)?.setText(json)
        val request = Request()
        requestViewModel.setRequest(request)

        val destinationText: AppCompatAutoCompleteTextView =
            view.findViewById(R.id.select_destination_text)
        destinationText.threshold = 2;
        destinationText.setAdapter(destinationLocationSuggestionAdapter);
        destinationText.onItemClickListener = AdapterView.OnItemClickListener(fun(
            _: AdapterView<*>,
            _: View,
            position: Int,
            _: Long
        ) {
            val selectedLocation = destinationLocationSuggestionAdapter.getObject(position)
            requestViewModel.setDestination(
                LocationModel(
                    name = selectedLocation.displayName,
                    latitude = selectedLocation.lat,
                    longitude = selectedLocation.lon
                )
            )
        })


        destinationText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                destinationHandler.removeMessages(TRIGGER_AUTO_COMPLETE)
                destinationHandler.sendEmptyMessageDelayed(
                    TRIGGER_AUTO_COMPLETE,
                    AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        destinationHandler = Handler { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(destinationText.text)) {
                    makeApiCall(
                        destinationText.text.toString(),
                        destinationLocationSuggestionAdapter
                    )
                }
            }
            false
        }

        view.findViewById<CardView>(R.id.recent_destinations_cardView).setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_gallery)
        }

        view.findViewById<Button>(R.id.go_button).setOnClickListener {
            requestViewModel.selectedRequest.observe(viewLifecycleOwner, Observer { request ->
                requestViewModel.createRequest(request, requireContext())

                requestViewModel.createdRequestLiveData?.observe(viewLifecycleOwner, Observer {
                    Log.d("Created Request:", request.toString())
                    Toast.makeText(this.requireContext(), "request created", Toast.LENGTH_SHORT)
                            .show()
//                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    val userId = User()._id
                    val location=LocationModel().toString()
                    val gson = Gson()
                    val json = gson.toJson(mLocation)

                    with (sharedPref?.edit()) {
                        this?.putString(userId,json)
                        this?.apply()
                    }
//                    editor.putString(userId, location)
//                    editor.apply();
                    Toast.makeText(this.requireContext(), "shared preference done", Toast.LENGTH_SHORT)
                            .show()
                    findNavController().navigate(R.id.action_nav_home_to_nav_gallery)

                })
            })

        }
    }

    private fun makeApiCall(text: String, adapter: LocationSuggestionAdapter) {
        val locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        locationViewModel.fetchAllLocations(text)

        locationViewModel.locationListLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.setData(it as ArrayList<LocationModel>)
            } else {
                Toast.makeText(this.requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        Log.d("LOCATION", "In get last location")
        if (checkPermissions()) {
            Log.d("LOCATION", "After check permissions")
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation
                    .addOnCompleteListener { task ->
                        val location: Location? = task.result
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
                            requestViewModel.setRiderLocation(
                                LocationModel(
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
            val mLastLocation: Location = locationResult.lastLocation
            Toast.makeText(
                requireContext(),
                "Location Retrieved",
                Toast.LENGTH_LONG
            ).show()
            requestViewModel.setRiderLocation(
                LocationModel(
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
        val locationManager = getSystemService(requireContext(), LocationManager::class.java)
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
}