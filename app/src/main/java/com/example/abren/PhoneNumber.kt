package com.example.abren

//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.ui.AppBarConfiguration
//import com.example.abren.databinding.ActivityPhoneNumberBinding
//import com.example.abren.databinding.ActivityRiderRoutesHomeBinding
//import com.google.firebase.FirebaseException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthOptions
//import com.google.firebase.auth.PhoneAuthProvider
////import kotlinx.android.synthetic.main.activity_phone_number.*
//import java.util.concurrent.TimeUnit
//
//class PhoneNumber : AppCompatActivity() {
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityPhoneNumberBinding
//    lateinit var auth: FirebaseAuth
//    lateinit var storedVerificationId:String
//    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
//    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
////        setContentView(binding.root)
//        setContentView(R.layout.activity_phone_number)
//        auth=FirebaseAuth.getInstance()
//        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
//        val login=findViewById<Button>(R.id.send_authorization_code_button)
//        var currentUser = auth.currentUser
//        if(currentUser != null) {
//            Toast.makeText(applicationContext, "signed in", Toast.LENGTH_SHORT).show()
////            startActivity(Intent(applicationContext, home::class.java))
//            finish()
//        }
//
//        login.setOnClickListener{
//            login()
//        }
//        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
////                startActivity(Intent(applicationContext, home::class.java))
//                Toast.makeText(applicationContext, "Successful", Toast.LENGTH_LONG).show()
//                finish()
//            }
//
//            override fun onVerificationFailed(e: FirebaseException) {
//                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
//            }
//
//            override fun onCodeSent(
//                verificationId: String,
//                token: PhoneAuthProvider.ForceResendingToken
//            ) {
//
//                Log.d("TAG","onCodeSent:$verificationId")
//                storedVerificationId = verificationId
//                resendToken = token
////                var intent = Intent(applicationContext,Otp::class.java)
//                intent.putExtra("storedVerificationId",storedVerificationId)
//                startActivity(intent)
//            }
//        }
//    }
//
//
//    private fun login(){
//        val mobileNumber=findViewById<EditText>(R.id.phone_number_auth)
//        var number=mobileNumber.text.toString().trim()
//
//        if(!number.isEmpty()){
//            number="+251"+number
//            sendVerificationcode (number)
//        }else{
//            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun sendVerificationcode(number: String) {
//        val options = PhoneAuthOptions.newBuilder(auth)
//            .setPhoneNumber(number) // Phone number to verify
//            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//            .setActivity(this) // Activity (for callback binding)
//            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
//            .build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//}
//
