package com.example.abren
//
//import com.example.abren.databinding.Fragment
//import android.R
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthProvider
//
//
//class Otp : AppCompatActivity() {
//
//
//    lateinit var auth: FirebaseAuth
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_otp)
//        auth=FirebaseAuth.getInstance()
//        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
//        val verify=findViewById<Button>(R.id.submit_button)
//        val otpGiven=findViewById<EditText>(R.id.otp_val)
//        val storedVerificationId=intent.getStringExtra("storedVerificationId")
//        verify.setOnClickListener{
//            var otp=otpGiven.text.toString().trim()
//            if(!otp.isEmpty()){
//                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
//                    storedVerificationId.toString(), otp)
//                signInWithPhoneAuthCredential(credential)
//            }else{
//                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
//            }
//        }
//        }
//
//    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(applicationContext, "Successful", Toast.LENGTH_LONG).show()
////                    startActivity(Intent(applicationContext, home::class.java))
//                    finish()
//// ...
//                } else {
//// Sign in failed, display a message and update the UI
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//// The verification code entered was invalid
//                        Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//    }
//    }
//
//
//
