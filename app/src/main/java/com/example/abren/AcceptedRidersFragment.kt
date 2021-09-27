import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.abren.R
import com.example.abren.viewmodel.RequestViewModel
import com.example.abren.viewmodel.RideViewModel

class AcceptedRidersFragment : Fragment(R.layout.fragment_rider_accepted) {
    private val rideViewModel: RideViewModel by activityViewModels()
    private val requestViewModel: RequestViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rider_accepted, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val driverFinishedLayout = view.findViewById<LinearLayout>(R.id.driverFinishedLayout)

        val amountText = view.findViewById<TextView>(R.id.driverAmountText)

        val nextButton = view.findViewById<Button>(R.id.driverNextButton2)
        val prevButton = view.findViewById<Button>(R.id.driverPrevButton2)
        val driverFinishButton = view.findViewById<TextView>(R.id.driverFinishButton)

        val otpText = view.findViewById<TextView>(R.id.otpText)
        val acceptedCountText = view.findViewById<TextView>(R.id.acceptedCountText)
        val inRideCountText = view.findViewById<TextView>(R.id.inRideCountText)

        val riderGenderText = view.findViewById<TextView>(R.id.riderGenderText2)
        val riderAgeGroupText = view.findViewById<TextView>(R.id.riderAgeGroupText2)
        val riderRatingBar = view.findViewById<RatingBar>(R.id.riderRatingBar2)
        val riderDestinationText = view.findViewById<TextView>(R.id.riderDestinationText2)
        val riderStatusText = view.findViewById<TextView>(R.id.statusText)
        val riderNumber = view.findViewById<TextView>(R.id.riderNumber2)


        requestViewModel.currentRequestsLiveData?.observe(viewLifecycleOwner, Observer { requests ->
            if (requests != null) {
                otpText.text = rideViewModel.createdRideLiveData?.value?.otp?.code

                requestViewModel.currentAccepted?.observe(viewLifecycleOwner, Observer { index ->
                    if (index != null) {
                        prevButton.isEnabled = index != 0
                        nextButton.isEnabled = index >= 0 && index != requests.accepted.size - 1

                        if (!requests.accepted.isNullOrEmpty()) {
                            val inRide = requests.accepted.count { it?.status == "ACCEPTED" }
                            acceptedCountText.text = inRide.toString()
                            inRideCountText.text = (requests.accepted.size - inRide).toString()

                            val current = requests.accepted[index]
                            riderNumber.text = "${index + 1}"
                            riderGenderText.text = current?.riderGender
                            riderAgeGroupText.text = current?.riderAgeGroup
                            riderRatingBar.rating = calculateRating(current?.riderRating!!)
                            riderDestinationText.text = current.destination?.name.toString()

                            riderStatusText.text =
                                if (current.status == "STARTED") "In Ride" else if (current.status == "ACCEPTED") "Waiting" else "Finished"
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
            requestViewModel.setNextAccepted()
        }

        prevButton.setOnClickListener {
            requestViewModel.setPrevAccepted()
        }

        driverFinishButton.setOnClickListener {
            rideViewModel.finishRide(
                rideViewModel.createdRideLiveData?.value?._id!!,
                rideViewModel.kmLiveData?.value.toString(),
                requireContext()
            )
            rideViewModel.finishedRideLiveData?.observe(viewLifecycleOwner, Observer { ride ->
                if (ride != null) {
                    driverFinishedLayout.visibility = View.VISIBLE
                    val acceptedNum =
                        requestViewModel.currentRequestsLiveData?.value?.accepted?.size?.times(
                            ride.cost
                        )
                    amountText.text = "${String.format("%.2f", acceptedNum)} birr"
                }

            })

        }


    }

    private fun calculateRating(ratingInput: MutableList<Int>): Float {
        var prod = 0
        for (i in 0 until ratingInput.size) {
            prod += ((i + 1) * ratingInput[i])
        }

        return (prod.toFloat()) / ratingInput.sum()
    }
}




