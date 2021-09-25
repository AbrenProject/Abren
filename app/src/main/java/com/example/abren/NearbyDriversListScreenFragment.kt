package com.example.abren

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anton46.stepsview.StepsView
import com.example.abren.viewmodel.RequestViewModel
import com.example.abren.viewmodel.RideViewModel


class NearbyDriversListScreenFragment : Fragment(R.layout.fragment_nearby_drivers_list_screen) {

    private val rideViewModel: RideViewModel by activityViewModels()
    private val requestViewModel: RequestViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nearby_drivers_list_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sendButton = view.findViewById<Button>(R.id.sendButton)
        val nextButton = view.findViewById<Button>(R.id.riderNextButton)
        val prevButton = view.findViewById<Button>(R.id.riderPrevButton)

        val genderText = view.findViewById<TextView>(R.id.driverGenderText1)
        val ageGroupText = view.findViewById<TextView>(R.id.driverAgeGroupText1)
        val ratingBar = view.findViewById<RatingBar>(R.id.driverRatingBar1)
        val routeStartText = view.findViewById<TextView>(R.id.routeStartText)
        val routeWaypointText = view.findViewById<TextView>(R.id.routeWaypointText)
        val routeDestinationText = view.findViewById<TextView>(R.id.routeDestinationText)
        val numberText = view.findViewById<TextView>(R.id.driverNumberText1)
        val nearbyCountText = view.findViewById<TextView>(R.id.nearbyCountText)

        val routeView = view.findViewById<StepsView>(R.id.routeView)

        rideViewModel.nearbyRidesLiveData?.observe(viewLifecycleOwner, Observer { rides ->
            if (rides != null) {
                rideViewModel.currentNearby?.observe(viewLifecycleOwner, Observer { index ->
                    if (index != null) {
                        prevButton.isEnabled = index != 0
                        nextButton.isEnabled = index >= 0 && index != rides.nearby.size - 1

                        if (!rides.nearby.isNullOrEmpty()) {
                            nearbyCountText.text = rides.nearby.size.toString()
                            val current = rides.nearby[index]
                            numberText.text = "${index + 1}"
                            genderText.text = current?.driverGender
                            ageGroupText.text = current?.driverAgeGroup
                            ratingBar.rating = calculateRating(current?.driverRating!!)

                            routeWaypointText.text = ""

                            for(i in 0 until current.route?.waypointLocations?.size!!){
                                routeWaypointText.text = routeWaypointText.text.toString() + "${current.route?.waypointLocations?.get(i)?.name!!} > "
                            }

                            routeStartText.text = "${current.route?.startingLocation?.name!!} > "
                            routeDestinationText.text = "${current.route?.destinationLocation?.name!!}"

                        }
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "Something Went Wrong",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
            } else {
                Toast.makeText(this.requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        sendButton.setOnClickListener {
            makeSendRequestApiCall()
        }

        nextButton.setOnClickListener {
            rideViewModel.setNextNearby()
        }

        prevButton.setOnClickListener {
            rideViewModel.setPrevNearby()
        }
    }

    private fun calculateRating(ratingInput: MutableList<Int>): Float {
        var prod = 0
        for (i in 0 until ratingInput.size) {
            prod += ((i + 1) * ratingInput[i])
        }

        return (prod.toFloat()) / ratingInput.sum()
    }

    private fun makeSendRequestApiCall() {
        Log.d("REQUEST", "Making api call - send request")

        requestViewModel.createdRequestLiveData?.observe(viewLifecycleOwner, Observer { request ->
            if (request != null && rideViewModel.nearbyRidesLiveData?.value?.nearby?.size != 0) {
                requestViewModel.sendRequest(
                    request._id!!,
                    rideViewModel.nearbyRidesLiveData?.value?.nearby?.get(rideViewModel.currentNearby?.value!!)?._id!!,
                    requireContext()
                )
            } else {
                Log.d("REQUEST", "Problem in making api call")
//                Toast.makeText(this.requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
//                    .show()
            }
        })
    }
}

