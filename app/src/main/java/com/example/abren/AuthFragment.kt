package com.example.abren

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
import androidx.navigation.fragment.findNavController
import com.example.abren.viewmodel.UserViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthFragment : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()
    lateinit var auth: FirebaseAuth
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.register_button_auth).setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_RegisterFragment)
        }
        auth = FirebaseAuth.getInstance()
        val loginButton = view.findViewById<Button>(R.id.send_authorization_code_button)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(requireContext(), "signed in", Toast.LENGTH_SHORT).show()
        }

        loginButton.setOnClickListener {
            login()
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Toast.makeText(context?.applicationContext, "Verification Successful", Toast.LENGTH_LONG).show()
                activity?.finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context?.applicationContext, e.message, Toast.LENGTH_LONG).show()
                Log.d("FIREBASE", e.message.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("FIREBASE", "onCodeSent:$verificationId")
                userViewModel.loginPhoneNumber = view.findViewById<EditText>(R.id.phone_number_auth).text.toString().trim()
                userViewModel.storedVerificationId = verificationId
                resendToken = token

                Toast.makeText(requireContext(), "Code Send Successful", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_authFragment_to_otpFragment)
            }
        }
    }


    private fun login() {
        val mobileNumber = view?.findViewById<EditText>(R.id.phone_number_auth)
        var number = mobileNumber?.text.toString().trim()

        if (number.isNotEmpty()) {
            number = "+251$number"
            sendVerificationCode(number)
        } else {
            Toast.makeText(activity, "Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}
