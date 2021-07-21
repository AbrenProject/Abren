package com.example.abren

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class RegisterForm2Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_form2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.back_button2).setOnClickListener{
            findNavController().navigate(R.id.action_RegisterForm2Fragment_to_RegisterForm1Fragment)
        }

        view.findViewById<Button>(R.id.continue_button2).setOnClickListener{
            findNavController().navigate(R.id.action_RegisterForm2Fragment_to_PreferenceFragment)
        }
    }
}