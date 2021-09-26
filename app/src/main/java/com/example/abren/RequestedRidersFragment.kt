import android.os.Bundle
import android.util.Log
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
import com.example.abren.R
import com.example.abren.viewmodel.RequestViewModel
import com.example.abren.viewmodel.RideViewModel

class RequestedRidersFragment : Fragment(R.layout.fragment_rider_requests) {

    private val rideViewModel: RideViewModel by activityViewModels()
    private val requestViewModel: RequestViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rider_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val acceptButton = view.findViewById<Button>(R.id.acceptButton)
        val nextButton = view.findViewById<Button>(R.id.driverNextButton)
        val prevButton = view.findViewById<Button>(R.id.driverPrevButton)

        val riderGenderText = view.findViewById<TextView>(R.id.riderGenderText)
        val riderAgeGroupText = view.findViewById<TextView>(R.id.riderAgeGroupText)
        val riderRatingBar = view.findViewById<RatingBar>(R.id.riderRatingBar)
        val riderDestinationText = view.findViewById<TextView>(R.id.riderDestinationText)
        val requestedNumber = view.findViewById<TextView>(R.id.requestsCountText)
        val riderNumber = view.findViewById<TextView>(R.id.riderNumber)


        requestViewModel.currentRequestsLiveData?.observe(viewLifecycleOwner, Observer { requests ->
            if (requests != null) {
                requestViewModel.currentRequested?.observe(viewLifecycleOwner, Observer { index ->
                    if (index != null) {
                        prevButton.isEnabled = index != 0
                        nextButton.isEnabled = index > 0 && index != requests.requested.size - 1

                        if (!requests.requested.isNullOrEmpty()) {
                            requestedNumber.text = requests.requested.size.toString()
                            val current = requests.requested[index]
                            riderNumber.text = "${index + 1}"
                            riderGenderText.text = current?.riderGender
                            riderAgeGroupText.text = current?.riderAgeGroup
                            riderRatingBar.rating = calculateRating(current?.riderRating!!)
                            riderDestinationText.text = current.destination?.name.toString()
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

        acceptButton.setOnClickListener {
            makeAcceptedRequestApiCall()
        }

        nextButton.setOnClickListener {
            requestViewModel.setNextRequested()
        }

        prevButton.setOnClickListener {
            requestViewModel.setPrevRequested()
        }

    }

    private fun calculateRating(ratingInput: MutableList<Int>): Float {
        var prod = 0
        for (i in 0 until ratingInput.size) {
            prod += ((i + 1) * ratingInput[i])
        }

        return (prod.toFloat()) / ratingInput.sum()
    }

    private fun makeAcceptedRequestApiCall() {
        Log.d("REQUEST", "Making api call - accept request")

        rideViewModel.createdRideLiveData?.observe(viewLifecycleOwner, Observer { ride ->
            if (ride != null && requestViewModel.currentRequestsLiveData?.value?.requested?.size != 0) {
                rideViewModel.acceptRequest(
                    ride._id!!,
                    requestViewModel.currentRequestsLiveData?.value?.requested?.get(
                        rideViewModel.currentRequested?.value!!
                    )?._id!!,
                    requireContext()
                )
            } else {
                Log.d("REQUEST", "Problem in making api call")
                Toast.makeText(this.requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}





