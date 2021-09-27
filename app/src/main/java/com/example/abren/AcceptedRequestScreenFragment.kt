package com.example.abren

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.viewmodel.RequestViewModel
import com.example.abren.viewmodel.RideViewModel
import com.example.abren.viewmodel.UserViewModel
import com.mapbox.mapboxsdk.style.layers.Property

class AcceptedRequestScreenFragment : Fragment(R.layout.fragment_accepted_driver_viewing_screen) {

    private val rideViewModel: RideViewModel by activityViewModels()
    private val requestViewModel: RequestViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accepted_driver_viewing_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishedLayout = view.findViewById<LinearLayout>(R.id.finishedLayout)

        val validateButton = view.findViewById<ImageButton>(R.id.validateButton)
        val rateButton = view.findViewById<Button>(R.id.rateButton)

        val ratingBarInput = view.findViewById<RatingBar>(R.id.ratingBarInput)

        val genderText = view.findViewById<TextView>(R.id.driverGenderText3)
        val ageGroupText = view.findViewById<TextView>(R.id.driverAgeGroupText3)
        val ratingBar = view.findViewById<RatingBar>(R.id.driverRatingBar3)
        val makeText = view.findViewById<TextView>(R.id.driverCarMakeText)
        val modelText = view.findViewById<TextView>(R.id.driverCarModelText)
        val licenseNumberText = view.findViewById<TextView>(R.id.driverCarLicenseNumberText)
        val routeStartText = view.findViewById<TextView>(R.id.routeStartText3)
        val routeWaypointText = view.findViewById<TextView>(R.id.routeWaypointText3)
        val routeDestinationText = view.findViewById<TextView>(R.id.routeDestinationText3)

        val codeText = view.findViewById<EditText>(R.id.codeText)
        val amountText = view.findViewById<TextView>(R.id.amountText)

        rideViewModel.nearbyRidesLiveData?.observe(viewLifecycleOwner, Observer { rides ->
            if (rides != null) {
                if (rides.accepted != null) {
                    genderText.text = rides.accepted?.driverGender
                    ageGroupText.text = rides.accepted?.driverAgeGroup
                    ratingBar.rating = calculateRating(rides.accepted?.driverRating!!)
                    makeText.text = rides.accepted?.vehicleMake
                    modelText.text = rides.accepted?.vehicleModel
                    licenseNumberText.text = rides.accepted?.vehiclePlateNumber

                    routeWaypointText.text = ""

                    for (i in 0 until rides.accepted?.route?.waypointLocations?.size!!) {
                        routeWaypointText.text = routeWaypointText.text.toString() + "${
                            rides.accepted?.route?.waypointLocations?.get(i)?.name?.substringBefore(",")!!
                        } > "
                    }

                    routeStartText.text = "${rides.accepted?.route?.startingLocation?.name?.substringBefore(",")!!} > "
                    routeDestinationText.text =
                        "${rides.accepted?.route?.destinationLocation?.name?.substringBefore(",")!!}"

                    if (rides.accepted?.status == "FINISHED") {
                        finishedLayout.visibility = View.VISIBLE
                        amountText.text = "${String.format("%.2f", rides.accepted?.cost)} birr"
                    }
                }
            }
        })

        validateButton.setOnClickListener {
            makeStartRideApiCall(codeText.text.toString())

            requestViewModel.acceptedRequestLiveData?.observe(
                viewLifecycleOwner,
                Observer { request ->
                    if (request != null) {
                        if (request.status == "STARTED") {
                            codeText.setText("Validated")
                            codeText.isEnabled = false
                            validateButton.setImageResource(R.drawable.ic_tick_white)
                        }
                    }
                })
        }

        rateButton.setOnClickListener {
            makeRateDriverApiCall(ratingBarInput.rating.toInt().toString())
            findNavController().popBackStack()
        }
    }

    private fun calculateRating(ratingInput: MutableList<Int>): Float {
        var prod = 0
        for (i in 0 until ratingInput.size) {
            prod += ((i + 1) * ratingInput[i])
        }

        return (prod.toFloat()) / ratingInput.sum()
    }

    private fun makeStartRideApiCall(otp: String) {
        Log.d("REQUEST", "Making api call - send request")

        requestViewModel.createdRequestLiveData?.observe(viewLifecycleOwner, Observer { request ->
            if (request != null) {
                requestViewModel.startRide(
                    request._id!!,
                    otp,
                    requireContext()
                )
            } else {
                Log.d("REQUEST", "Problem in making api call")
                Toast.makeText(this.requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun makeRateDriverApiCall(rating: String) {
        Log.d("REQUEST", "Making api call - rate user")

        userViewModel.rate(
            rideViewModel.nearbyRidesLiveData?.value?.accepted?.driverId!!,
            rating,
            requireContext()
        )
    }

}
