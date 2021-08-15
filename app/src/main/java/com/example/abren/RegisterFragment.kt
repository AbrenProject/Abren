package com.example.abren

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abren.models.User
import com.example.abren.viewmodel.UserViewModel


class RegisterFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) :View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = User()

        view.findViewById<Button>(R.id.give_ride_button).setOnClickListener {
            user.role = "DRIVER"
            viewModel.selectUser(user)
            findNavController().navigate(R.id.action_RegisterFragment_to_RegisterForm1Fragment)
        }

        view.findViewById<Button>(R.id.receive_ride_button).setOnClickListener{
            user.role = "RIDER"
            viewModel.selectUser(user)
            findNavController().navigate(R.id.action_RegisterFragment_to_RegisterForm1Fragment)
        }
    }

}