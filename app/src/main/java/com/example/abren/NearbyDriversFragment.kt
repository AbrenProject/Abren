package com.example.abren


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style


class NearbyDriversFragment : Fragment() , OnMapReadyCallback, PermissionsListener {

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var permissionManager: PermissionsManager? = null
    private var locationComponent: LocationComponent? = null



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
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            enableLocationComponent(style)
        }
    }

    private fun enableLocationComponent(loadedMapStyle: Style?) {
        //check if permission enabled if not request
        context?.let {
            if (PermissionsManager.areLocationPermissionsGranted(it.applicationContext)) {
                // activity the mapboxmap locationComponent to show userlocation
                // adding in locationcomponentOptions is also an optional paramenter
                locationComponent = mapboxMap!!.locationComponent
                context?.let {
                    locationComponent!!.activateLocationComponent(
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
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    locationComponent!!.setLocationComponentEnabled(true)

                    //set the component's camera mode
                    locationComponent!!.setCameraMode(CameraMode.TRACKING)
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
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
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
        mapView?.onDestroy()
    }

}




