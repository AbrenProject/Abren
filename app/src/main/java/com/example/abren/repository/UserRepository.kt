package com.example.abren.repository

import android.telephony.emergency.EmergencyNumber
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.User
import com.example.abren.network.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart

object UserRepository {

    val mutableSelectedUser = MutableLiveData<User>()
    val selectedUser:LiveData<User> get() = mutableSelectedUser
    val responseBody = MutableLiveData<ResponseBody>()


    fun getServicesApiCall(profileImage:MultipartBody.Part,
                           idCardImage:MultipartBody.Part,
                           idCardBackImage:MultipartBody.Part,
                           phoneNumber:String,
                           emergencyPhoneNumber:String,
                           role:String,
                           password:String): MutableLiveData<ResponseBody> {

        val call = RetrofitClient.apiInterface.registerUser(
           profileImage, idCardImage, idCardBackImage, phoneNumber,
            emergencyPhoneNumber, role, password
        )

        call.enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("UserRepo--- ONFailure", t.message.toString())
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("UserRepo--- On success",response.body().toString())
                Log.d("UserRepo--- On success",response.code().toString())
                Log.d("UserRepo--- On success",response.toString())
                val data = response.body()

            }
        } )
        return responseBody
    }

    fun getPasssword():String{
        val pass = User().password
        return pass
    }


}

