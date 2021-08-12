package com.example.abren

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.abren.adapter.LocationSuggestionAdapter
import com.example.abren.models.PostModel
import com.example.abren.viewmodel.PostViewModel
import org.json.JSONObject

import org.json.JSONArray
import java.lang.Exception
import android.text.TextUtils





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

    private lateinit var postViewModel:PostViewModel
    private lateinit var locationSuggestionAdapter: LocationSuggestionAdapter

    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        locationSuggestionAdapter = LocationSuggestionAdapter(
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val startText: AppCompatAutoCompleteTextView = view.findViewById(R.id.start_text)

        val selectedText: TextView = view.findViewById(R.id.selected_text)

        val onItemClickListener = fun (_: AdapterView<*>, _: View, position: Int, _: Long) {
            selectedText.text = locationSuggestionAdapter.getObject(position)?.title
        }

        startText.threshold = 2;
        startText.setAdapter(locationSuggestionAdapter);
        startText.onItemClickListener = AdapterView.OnItemClickListener(onItemClickListener)
        startText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE)
                handler.sendEmptyMessageDelayed(
                    TRIGGER_AUTO_COMPLETE,
                    AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        handler = Handler { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(startText.text)) {
                    makeApiCall(startText.text.toString())
                }
            }
            false
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

    private fun makeApiCall(text: String) {
        var postViewModel = ViewModelProvider(this)[PostViewModel::class.java]

        postViewModel.fetchAllPosts()

        postViewModel.postModelListLiveData?.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                locationSuggestionAdapter.setData(it as ArrayList<PostModel>)
            }else{
                Toast.makeText(this.requireContext(),"Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
}