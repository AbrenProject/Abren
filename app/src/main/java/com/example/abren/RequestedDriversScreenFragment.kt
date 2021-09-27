package com.example.abren

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.abren.viewmodel.RequestViewModel
import com.example.abren.viewmodel.RideViewModel

class RequestedDriversScreenFragment : Fragment(R.layout.fragment_requested_drivers) {

    private val rideViewModel: RideViewModel by activityViewModels()
    private val requestViewModel: RequestViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requested_drivers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val cancelButton = view.findViewById<Button>(R.id.cancelButton)
        val nextButton = view.findViewById<Button>(R.id.riderNextButton2)
        val prevButton = view.findViewById<Button>(R.id.riderPrevButton2)

        val genderText = view.findViewById<TextView>(R.id.driverGenderText2)
        val ageGroupText = view.findViewById<TextView>(R.id.driverAgeGroupText2)
        val ratingBar = view.findViewById<RatingBar>(R.id.driverRatingBar2)
        val routeStartText = view.findViewById<TextView>(R.id.routeStartText2)
        val routeWaypointText = view.findViewById<TextView>(R.id.routeWaypointText2)
        val routeDestinationText = view.findViewById<TextView>(R.id.routeDestinationText2)
        val numberText = view.findViewById<TextView>(R.id.driverNumberText2)
        val requestedCountText = view.findViewById<TextView>(R.id.requestedCountText)

        rideViewModel.nearbyRidesLiveData?.observe(viewLifecycleOwner, Observer { rides ->
            if (rides != null) {
                rideViewModel.currentRequested?.observe(viewLifecycleOwner, Observer { index ->
                    if (index != null) {
                        prevButton.isEnabled = index != 0
                        nextButton.isEnabled = index >= 0 && index != rides.requested.size - 1

                        if(!rides.requested.isNullOrEmpty()){
                            requestedCountText.text = rides.requested.size.toString()
                            val current = rides.requested[index]
                            numberText.text = "${index + 1}"
                            genderText.text = current?.driverGender
                            ageGroupText.text = current?.driverAgeGroup
                            ratingBar.rating = calculateRating(current?.driverRating!!)

                            routeWaypointText.text = ""

                            for(i in 0 until current.route?.waypointLocations?.size!!){
                                routeWaypointText.text = routeWaypointText.text.toString() + "${current.route?.waypointLocations?.get(i)?.name?.substringBefore(",")!!} > "
                            }

                            routeStartText.text = "${current.route?.startingLocation?.name?.substringBefore(",")!!} > "
                            routeDestinationText.text = "${current.route?.destinationLocation?.name?.substringBefore(",")!!}"
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

        nextButton.setOnClickListener {
            rideViewModel.setNextRequested()
        }

        prevButton.setOnClickListener {
            rideViewModel.setPrevRequested()
        }
    }



    private fun calculateRating(ratingInput: MutableList<Int>): Float {
        var prod = 0
        for(i in 0 until ratingInput.size){
            prod += ((i+1) * ratingInput[i])
        }

        return (prod.toFloat()) / ratingInput.sum()
    }
}