package com.example.abren.repository

import android.text.InputFilter
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.User
import com.example.abren.network.RetrofitClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

object UserRepository {

    private val responseBody = MutableLiveData<ResponseBody>()

    fun getServicesApiCall(profileImage:MultipartBody.Part,
                           idCardImage:MultipartBody.Part,
                           idCardBackImage:MultipartBody.Part,
                           phoneNumber:RequestBody,
                           emergencyPhoneNumber:RequestBody,
                           role:RequestBody,
                           password:RequestBody): MutableLiveData<ResponseBody> {

        val call = RetrofitClient.apiInterface.registerUser(
           profileImage, idCardImage, idCardBackImage, phoneNumber,
            emergencyPhoneNumber, role, password
        )

        call.enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("UserRepo--- ONFailure", t.message.toString())
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.i("RETROFIT", response.toString())
                    if (response.code() == 200 && response.body()!=null){
                        responseBody.value = response.body()
//                        Toast.makeText(this,"Registration Successful",0.2).show()
                        Log.d("responsebody check on success",responseBody.value.toString())
                    }
                    Log.d("UserRepo--- response header = ", response.headers().toString())
                response.errorBody()?.string()?.let { Log.d("UserRepo---- body", it) }
                    Log.d("UserRepo--- Response ",response.toString())
            }
        } )
        return responseBody
    }

    fun getPasssword(): String {
        return User().password
    }

}

