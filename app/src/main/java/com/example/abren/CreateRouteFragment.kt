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
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.abren.adapter.LocationSuggestionAdapter
import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.example.abren.models.Location
import com.example.abren.viewmodel.LocationViewModel
import androidx.constraintlayout.widget.ConstraintSet





// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateRouteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateRouteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val TRIGGER_AUTO_COMPLETE = 100
    private val AUTO_COMPLETE_DELAY: Long = 300

    private lateinit var locationViewModel: LocationViewModel
    private lateinit var startLocationSuggestionAdapter: LocationSuggestionAdapter
    private lateinit var destinationLocationSuggestionAdapter: LocationSuggestionAdapter
    private lateinit var waypointLocationSuggestionAdapter: LocationSuggestionAdapter

    private lateinit var startHandler: Handler
    private lateinit var destinationHandler: Handler
    private lateinit var waypointHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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

        val startText: AppCompatAutoCompleteTextView = view.findViewById(R.id.start_text)
        val destinationText: AppCompatAutoCompleteTextView = view.findViewById(R.id.destination_text)
        val waypointText1: AppCompatAutoCompleteTextView = view.findViewById(R.id.waypoint_text1)

        val selectedText: TextView = view.findViewById(R.id.selected_text)

        startText.threshold = 2;
        startText.setAdapter(startLocationSuggestionAdapter);
        startText.onItemClickListener = AdapterView.OnItemClickListener(fun (_: AdapterView<*>, _: View, position: Int, _: Long) {
            selectedText.text = startLocationSuggestionAdapter.getObject(position).name
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

        destinationText.threshold = 2;
        destinationText.setAdapter(destinationLocationSuggestionAdapter);
        destinationText.onItemClickListener = AdapterView.OnItemClickListener(fun (_: AdapterView<*>, _: View, position: Int, _: Long) {
            selectedText.text = destinationLocationSuggestionAdapter.getObject(position).name
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

        waypointText1.threshold = 2;
        waypointText1.setAdapter(waypointLocationSuggestionAdapter);
        waypointText1.onItemClickListener = AdapterView.OnItemClickListener(fun (_: AdapterView<*>, _: View, position: Int, _: Long) {
            selectedText.text = waypointLocationSuggestionAdapter.getObject(position).name
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
                if (!TextUtils.isEmpty(startText.text)) {
                    makeApiCall(destinationText.text.toString(), destinationLocationSuggestionAdapter)
                }
            }
            false
        }

        waypointHandler = Handler { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(startText.text)) {
                    makeApiCall(waypointText1.text.toString(), waypointLocationSuggestionAdapter)
                }
            }
            false
        }

        val layout = view.findViewById<ConstraintLayout>(R.id.create_route_layout)
        view.findViewById<AppCompatImageButton>(R.id.add_button).setOnClickListener{
            val waypointText2 = AppCompatAutoCompleteTextView(ContextThemeWrapper(this.requireContext(), R.style.App_EditText), null, 0)

            val set = ConstraintSet()

            waypointText2.id = View.generateViewId()

            val waypointText2Params = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            waypointText2.layoutParams = waypointText2Params

            layout.addView(waypointText2, 0)

            set.clone(layout)
            set.connect(waypointText2.id, ConstraintSet.TOP, waypointText1.id, ConstraintSet.BOTTOM, 20)
            set.connect(waypointText2.id, ConstraintSet.START, layout.id, ConstraintSet.START, 10)
            set.connect(waypointText2.id, ConstraintSet.END, layout.id, ConstraintSet.END, 10)
            set.applyTo(layout)

            Toast.makeText(this.requireContext(),layout.childCount.toString(), Toast.LENGTH_SHORT).show()

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateRouteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateRouteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun makeApiCall(text: String, adapter: LocationSuggestionAdapter) {
        var locationViewModel = ViewModelProvider(this)[LocationViewModel::class.java]

        locationViewModel.fetchAllLocations(text)

        locationViewModel.locationListLiveData?.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                adapter.setData(it as ArrayList<Location>)
            }else{
                Toast.makeText(this.requireContext(),"Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
}