package com.example.abren

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.viewmodel.UserViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PreferenceFormFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preference_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.finish_button).setOnClickListener{
            Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
            viewModel.selectedUser.observe(viewLifecycleOwner, Observer { user ->
                viewModel.registerUser(user)

                viewModel.registeredUserLiveData?.observe(viewLifecycleOwner, Observer {
                    if (it!=null){
                        findNavController().navigate(R.id.action_PreferenceFragment_to_authFragment)
                    }else{
                        Toast.makeText(this.requireContext(),"Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }
                })
            })
        }
    }


}