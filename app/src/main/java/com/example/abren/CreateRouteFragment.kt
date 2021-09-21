package com.example.abren

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.abren.adapter.LocationSuggestionAdapter
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abren.models.Location
import com.example.abren.models.Route
import com.example.abren.viewmodel.LocationViewModel
import com.example.abren.viewmodel.RouteViewModel

private val TRIGGER_AUTO_COMPLETE = 100
private val AUTO_COMPLETE_DELAY: Long = 300

class CreateRouteFragment : Fragment() {
    private val routeViewModel: RouteViewModel by activityViewModels()

    private lateinit var startLocationSuggestionAdapter: LocationSuggestionAdapter
    private lateinit var destinationLocationSuggestionAdapter: LocationSuggestionAdapter
    private lateinit var waypointLocationSuggestionAdapter: LocationSuggestionAdapter

    private lateinit var startHandler: Handler
    private lateinit var destinationHandler: Handler
    private lateinit var waypointHandler: Handler

    var wayPointLocationId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startLocationSuggestionAdapter = LocationSuggestionAdapter(
                this.requireContext(),
                android.R.layout.simple_dropdown_item_1line
        )

        destinationLocationSuggestionAdapter = LocationSuggestionAdapter(
                this.requireContext(),
                android.R.layout.simple_dropdown_item_1line
        )

        waypointLocationSuggestionAdapter = LocationSuggestionAdapter(
                this.requireContext(),
                android.R.layout.simple_dropdown_item_1line
        )
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_route, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1) //TODO: Look into this more
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val route = Route()
        routeViewModel.setRoute(route)

        val startText: AppCompatAutoCompleteTextView = view.findViewById(R.id.start_text)
        val destinationText: AppCompatAutoCompleteTextView =
                view.findViewById(R.id.destination_text)
        val waypointText1: AppCompatAutoCompleteTextView = view.findViewById(R.id.waypoint_text1)

        startText.threshold = 2
        startText.setAdapter(startLocationSuggestionAdapter)
        startText.onItemClickListener = AdapterView.OnItemClickListener(fun(
                _: AdapterView<*>,
                _: View,
                position: Int,
                _: Long
        ) {
            val selectedLocation = startLocationSuggestionAdapter.getObject(position)
            routeViewModel.setStartLocation(
                    Location(
                            name = selectedLocation.displayName,
                            latitude = selectedLocation.lat,
                            longitude = selectedLocation.lon
                    )
            )
        })
        startText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                startHandler.removeMessages(TRIGGER_AUTO_COMPLETE)
                startHandler.sendEmptyMessageDelayed(
                        TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        destinationText.threshold = 2
        destinationText.setAdapter(destinationLocationSuggestionAdapter)
        destinationText.onItemClickListener = AdapterView.OnItemClickListener(fun(
                _: AdapterView<*>,
                _: View,
                position: Int,
                _: Long
        ) {
            val selectedLocation = destinationLocationSuggestionAdapter.getObject(position)
            routeViewModel.setDestinationLocation(
                    Location(
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

        waypointText1.threshold = 2
        waypointText1.setAdapter(waypointLocationSuggestionAdapter)
        waypointText1.onItemClickListener = AdapterView.OnItemClickListener(fun(
                _: AdapterView<*>,
                _: View,
                position: Int,
                _: Long
        ) {
            val selectedLocation = waypointLocationSuggestionAdapter.getObject(position)
            routeViewModel.addWaypointLocation(
                    Location(
                            name = selectedLocation.displayName,
                            latitude = selectedLocation.lat,
                            longitude = selectedLocation.lon
                    )
            )
        })
        waypointText1.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                waypointHandler.removeMessages(TRIGGER_AUTO_COMPLETE)
                waypointHandler.sendEmptyMessageDelayed(
                        TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        startHandler = Handler { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(startText.text)) {
                    makeApiCall(startText.text.toString(), startLocationSuggestionAdapter)
                }
            }
            false
        }

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

        waypointHandler = Handler { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(waypointText1.text)) {
                    makeApiCall(waypointText1.text.toString(), waypointLocationSuggestionAdapter)
                }
            }
            false
        }

        view.findViewById<AppCompatImageButton>(R.id.add_button).setOnClickListener {
            createEditTextView(view.findViewById(R.id.parentLayout))
        }

        view.findViewById<Button>(R.id.save_button).setOnClickListener {
            routeViewModel.selectedRoute.observe(viewLifecycleOwner, Observer { route ->
                routeViewModel.createRoute(route, requireContext())

                routeViewModel.createdRouteLiveData?.observe(viewLifecycleOwner, Observer {
                    Log.d("Created Request:", route.toString())
                    findNavController().navigate(R.id.action_createRouteFragment_to_driverHomeFragment)
                })
            })
        }
    }

    private fun createEditTextView(parentLayout: LinearLayout) {
        val newWaypointLocationText: AutoCompleteTextView = LayoutInflater.from(requireContext()).inflate(R.layout.auto_complete_edit_text, null) as AutoCompleteTextView

        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
        params.setMargins(20, 10, 10, 20)
        newWaypointLocationText.layoutParams = params

        newWaypointLocationText.hint = "Enter Waypoint ${++wayPointLocationId}"
        newWaypointLocationText.id = wayPointLocationId

        val newWaypointLocationSuggestionAdapter = LocationSuggestionAdapter(
                this.requireContext(),
                android.R.layout.simple_dropdown_item_1line
        )

        val newWaypointHandler = Handler { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(newWaypointLocationText.text)) {
                    makeApiCall(newWaypointLocationText.text.toString(), newWaypointLocationSuggestionAdapter)
                }
            }
            false
        }

        newWaypointLocationText.threshold = 2
        newWaypointLocationText.setAdapter(newWaypointLocationSuggestionAdapter)
        newWaypointLocationText.onItemClickListener = AdapterView.OnItemClickListener(fun(
                _: AdapterView<*>,
                _: View,
                position: Int,
                _: Long
        ) {
            val selectedLocation = newWaypointLocationSuggestionAdapter.getObject(position)
            routeViewModel.addWaypointLocation(
                    Location(
                            name = selectedLocation.displayName,
                            latitude = selectedLocation.lat,
                            longitude = selectedLocation.lon
                    )
            )
        })
        newWaypointLocationText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                newWaypointHandler.removeMessages(TRIGGER_AUTO_COMPLETE)
                newWaypointHandler.sendEmptyMessageDelayed(
                        TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        parentLayout.addView(newWaypointLocationText, parentLayout.size - 1)
    }

    private fun makeApiCall(text: String, adapter: LocationSuggestionAdapter) {
        val locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        locationViewModel.fetchAllLocations(text)

        locationViewModel.locationListLiveData?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.setData(it as ArrayList<Location>)
            } else {
                Toast.makeText(this.requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                        .show()
            }
        })
    }
}