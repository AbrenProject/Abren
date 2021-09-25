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

        val otpDetail = view.findViewById<TextView>(R.id.OTPcodeText)
        val nextButton = view.findViewById<Button>(R.id.riderNextButton)
        val prevButton = view.findViewById<Button>(R.id.riderPrevButton)

        val riderGenderText = view.findViewById<TextView>(R.id.riderGenderText)
        val riderAgeGroupText = view.findViewById<TextView>(R.id.riderAgeGroupText)
        val riderRatingBar = view.findViewById<RatingBar>(R.id.riderRatingBar)
        val riderDestinationAnswer = view.findViewById<TextView>(R.id.riderDestinationAnswer)


        requestViewModel.acceptedRequestLiveData?.observe(viewLifecycleOwner, Observer { requests ->
            if (requests != null) {
                requestViewModel.currentRequested?.observe(viewLifecycleOwner, Observer { index ->
                    if (index != null) {
                        prevButton.isEnabled = index != 0
                        nextButton.isEnabled = index > 0 && index != requests.accepted.size - 1

                        if(!requests.requestedRides.isNullOrEmpty()){
                            val current = requests.accepted[index]
                            riderGenderText.text = current?.riderGender
                            riderAgeGroupText.text = current?.riderAgeGroup
                            riderRatingBar.rating = calculateRating(current?.riderRating!!)
                            riderDestinationAnswer.text= current?.riderDestination
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
            requestViewModel.setNextRequested()
        }

        prevButton.setOnClickListener {
            requestViewModel.setPrevRequested()
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
