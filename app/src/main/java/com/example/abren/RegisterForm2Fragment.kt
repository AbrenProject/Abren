package com.example.abren

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.adapter.LocationSuggestionAdapter
import com.example.abren.viewmodel.UserViewModel
import android.widget.AdapterView

import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.ViewModelProvider
import com.example.abren.adapter.VehicleInformationAdapter
import com.example.abren.models.Location
import com.example.abren.models.MenuItem
import com.example.abren.models.VehicleInformation
import com.example.abren.viewmodel.LocationViewModel
import com.example.abren.viewmodel.VehicleInformationViewModel
import java.time.Year


class RegisterForm2Fragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var vehicleYearAdapter: VehicleInformationAdapter
    private lateinit var vehicleMakeAdapter: VehicleInformationAdapter
    private lateinit var vehicleModelAdapter: VehicleInformationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vehicleYearAdapter = VehicleInformationAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )

        vehicleMakeAdapter = VehicleInformationAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )

        vehicleModelAdapter = VehicleInformationAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_form2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        makeApiCall("YEARS", vehicleYearAdapter)
        val vehicleYear = view.findViewById<Spinner>(R.id.vehicle_year)
        vehicleYear.adapter = vehicleYearAdapter
        vehicleYear.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                userViewModel.setVehicleYear(vehicleYearAdapter.getObject(position).value)
                makeApiCall("MAKES", vehicleMakeAdapter, vehicleYearAdapter.getObject(position).value)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        val vehicleMake = view.findViewById<Spinner>(R.id.vehicle_make)
        vehicleMake.adapter = vehicleMakeAdapter
        vehicleMake.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                userViewModel.selectedUser.observe(viewLifecycleOwner, Observer { user ->
                    user.vehicleInformation?.year?.let {
                        makeApiCall("MODELS", vehicleModelAdapter,
                            it, vehicleMakeAdapter.getObject(position).value)
                    }
                })
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        val vehicleModel = view.findViewById<Spinner>(R.id.vehicle_model)
        vehicleModel.adapter = vehicleModelAdapter
        vehicleModel.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                Toast.makeText(requireContext(),
                    vehicleModelAdapter.getObject(position).value,
                    Toast.LENGTH_LONG)
                    .show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        view.findViewById<Button>(R.id.back_button2).setOnClickListener {
            findNavController().navigate(R.id.action_RegisterForm2Fragment_to_RegisterForm1Fragment)
        }

        view.findViewById<Button>(R.id.continue_button2).setOnClickListener {
            findNavController().navigate(R.id.action_RegisterForm2Fragment_to_PreferenceFragment)

        }
    }

    private fun makeApiCall(type: String, adapter: VehicleInformationAdapter, year: String = "", make: String = "") {
        val vehicleInformationViewModel = ViewModelProvider(this)[VehicleInformationViewModel::class.java]


        when (type) {
            "YEARS" -> {
                vehicleInformationViewModel.fetchYears()

                vehicleInformationViewModel.vehicleYearListLiveData?.observe(viewLifecycleOwner, Observer {
                    if (it!=null){
                        adapter.setData(it)
                    }else{
                        Toast.makeText(this.requireContext(),"Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            "MAKES" -> {
                vehicleInformationViewModel.fetchMakes(year)

                vehicleInformationViewModel.vehicleMakeListLiveData?.observe(viewLifecycleOwner, Observer {
                    if (it!=null){
                        adapter.setData(it)
                    }else{
                        Toast.makeText(this.requireContext(),"Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            "MODELS" -> {
                vehicleInformationViewModel.fetchModels(year, make)

                vehicleInformationViewModel.vehicleModelListLiveData?.observe(viewLifecycleOwner, Observer {
                    if (it!=null){
                        adapter.setData(it)
                    }else{
                        Toast.makeText(this.requireContext(),"Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

    }
}