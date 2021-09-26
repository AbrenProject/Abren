import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.abren.R
import com.example.abren.viewmodel.RequestViewModel
import com.example.abren.viewmodel.RideViewModel

class AcceptedRidersFragment:Fragment(R.layout.fragment_rider_accepted) {
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

//        val otpDetail = view.findViewById<TextView>(R.id.OTPcodeText)
//        val nextButton = view.findViewById<Button>(R.id.driverNextButton)
//        val prevButton = view.findViewById<Button>(R.id.driverPrevButton)
//
//        val riderGenderText = view.findViewById<TextView>(R.id.riderGenderText2)
//        val riderAgeGroupText = view.findViewById<TextView>(R.id.riderDestinationText2)
//        val riderRatingBar = view.findViewById<RatingBar>(R.id.riderRatingBar2)
//        val riderDestinationAnswer = view.findViewById<TextView>(R.id.riderDestinationAnswer)





//        requestViewModel.acceptedRiderRequestLiveData?.observe(viewLifecycleOwner, Observer { requests ->
//            if (requests != null) {
//                requestViewModel.currentRiderAcceptedRequest?.observe(viewLifecycleOwner, Observer { index ->
//                    if (index != null) {
//                        prevButton.isEnabled = index != 0
//                        nextButton.isEnabled = index > 0 && index != requests.accepted.size - 1
//
//                        if(!requests.accepted.isNullOrEmpty()){
//                            val current = requests.accepted[index]
//                            riderGenderText.text = current?.riderGender
//                            riderAgeGroupText.text = current?.riderAgeGroup
//                            riderRatingBar.rating = calculateRating(current?.riderRating!!)
//                            riderDestinationAnswer.text= current?.destination.toString()
//                         //   otpDetail.text= rideViewModel.acceptedRidesLiveData?
//                        }
//                    } else {
//                        Toast.makeText(
//                            this.requireContext(),
//                            "Something Went Wrong",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }
//                })
//            } else {
//                Toast.makeText(this.requireContext(), "Something Went Wrong", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//
//        )





//        nextButton.setOnClickListener {
//            requestViewModel.setNextRequested()
//        }
//
//        prevButton.setOnClickListener {
//            requestViewModel.setPrevRequested()
//        }


    }

//    private fun calculateRating(ratingInput: MutableList<Int>): Float {
//        var prod = 0
//        for(i in 0 until ratingInput.size){
//            prod += ((i+1) * ratingInput[i])
//        }
//
//        return (prod.toFloat()) / ratingInput.sum()
//    }



    }




