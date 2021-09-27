package com.example.abren

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.models.User
import com.example.abren.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.cloudinary.AccessControlRule.token

import android.content.SharedPreferences




class OtpFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = FirebaseAuth.getInstance()
        val verify = view.findViewById<Button>(R.id.submit_button)
        val otpGiven = view.findViewById<EditText>(R.id.otp_val)
        val storedVerificationId = userViewModel.storedVerificationId
        val phoneNumber = userViewModel.loginPhoneNumber


        verify.setOnClickListener {
            val otp = otpGiven.text.toString().trim()
            if (otp.isNotEmpty()) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(), otp
                )
                signInWithPhoneAuthCredential(credential, phoneNumber!!)
                Log.d("PhoneNumber: ", phoneNumber)

            } else {
                Toast.makeText(requireContext(), "Enter OTP", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        phoneNumber: String
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = User(phoneNumber = "251$phoneNumber")
                    userViewModel.login(user)
                    userViewModel.registeredUserLiveData?.observe(
                        viewLifecycleOwner,
                        Observer { authResponse ->
                            if (authResponse != null) {
                                val preferences = requireActivity().getSharedPreferences("ABREN", Context.MODE_PRIVATE)
                                preferences.edit().putString("TOKEN", authResponse.token).apply()
                                preferences.edit().remove("RECENT_DESTINATION").apply()
                                if (authResponse.user.role == "DRIVER") {
                                    findNavController().navigate(R.id.action_otpFragment_to_driverHomeFragment)
                                } else {
                                    findNavController().navigate(R.id.action_otpFragment_to_riderRoutesHome)
                                }
                            } else {
                                Toast.makeText(
                                    this.requireContext(),
                                    "Something Went Wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            context?.applicationContext,
                            "Invalid OTP",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }
}

