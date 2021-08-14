package com.example.abren

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.give_ride_button).setOnClickListener {
//            findNavController().navigate(R.id.action_RegisterFragment_to_RegisterForm1Fragment)
//        }
//
//        view.findViewById<Button>(R.id.receive_ride_button).setOnClickListener{
//            findNavController().navigate(R.id.action_RegisterFragment_to_RegisterForm1Fragment)
//        }
    }

}