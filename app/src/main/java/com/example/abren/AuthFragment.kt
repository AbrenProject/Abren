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
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AuthFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
//    override fun onCreate(savedInstanceState: Bundle?,view: View,context: Context) {
//        super.onCreate(savedInstanceState)
//
//
//    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.register_button_auth).setOnClickListener{
            findNavController().navigate(R.id.action_authFragment_to_RegisterFragment)
        }
        auth=FirebaseAuth.getInstance()
//        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        val login=view.findViewById<Button>(R.id.send_authorization_code_button)
        var currentUser = auth.currentUser
        if(currentUser != null) {
            Toast.makeText(context?.applicationContext, "signed in", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(applicationContext, home::class.java))
            activity?.finish()
        }

        login.setOnClickListener{
            login()
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                startActivity(Intent(applicationContext, home::class.java))
                Toast.makeText(context?.applicationContext, "Successful", Toast.LENGTH_LONG).show()
                activity?.finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context?.applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                val otpFragment=OtpFragment()
                val transaction: FragmentTransaction = getParentFragmentManager().beginTransaction()
                val bundle = Bundle()
                bundle.putString("storedVerificationId",storedVerificationId)
                otpFragment.setArguments(bundle); //data being send to SecondFragment
                transaction.add(R.id.nav_host_fragment, otpFragment);
                transaction.commit();
//                view.findViewById<Button>(R.id.send_authorization_code_button).setOnClickListener {
//                    findNavController().navigate(R.id.action_authFragment_to_PhoneNumberFragment)
//                }

            Toast.makeText(context?.applicationContext, "Successful", Toast.LENGTH_LONG).show()
        }
    }}


    private fun login(){
        val mobileNumber=view?.findViewById<EditText>(R.id.phone_number_auth)
        var number=mobileNumber?.text.toString().trim()

        if(!number.isEmpty()){
            number="+251"+number
            sendVerificationcode (number)
        }else{
            Toast.makeText(activity,"Enter mobile number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
//        view.findViewById<Button>(R.id.send_authorization_code_button).setOnClickListener{
//            findNavController().navigate(R.id.action_authFragment_to_PhoneNumberFragment)
//        }
//

    }
