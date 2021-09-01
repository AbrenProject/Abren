package com.example.abren

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OtpFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(context?.applicationContext,"I am here", Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            auth= FirebaseAuth.getInstance()
//            auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
            val verify=view.findViewById<Button>(R.id.submit_button)
            val otpGiven=view.findViewById<EditText>(R.id.otp_val)
            val bundle = arguments
            val storedVerificationId = bundle!!.getString("storedVerificationId")


            verify.setOnClickListener{
                var otp=otpGiven.text.toString().trim()
                if(!otp.isEmpty()){
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId.toString(), otp)
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(context?.applicationContext,"Enter OTP", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val homeFragment=HomeFragment()
                        val transaction: FragmentTransaction = getParentFragmentManager().beginTransaction()
                        //data being send to SecondFragment
                        transaction.add(R.id.nav_host_fragment, homeFragment);
                        transaction.commit();
                        Toast.makeText(context?.applicationContext, "Successful", Toast.LENGTH_LONG).show()

//                        startActivity(Intent(applicationContext, home::class.java))
//                        activity?.finish()
// ...
                    } else {
// Sign in failed, display a message and update the UI
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
// The verification code entered was invalid
                            Toast.makeText(context?.applicationContext,"Invalid OTP", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
//
//
        }

