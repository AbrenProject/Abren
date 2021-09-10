package com.example.abren.repository

import android.util.Log
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
import

object UserRepository {

    val mutableSelectedUser = MutableLiveData<User>()
    val selectedUser:LiveData<User> get() = mutableSelectedUser


    lateinit var profileImage: MultipartBody.Part
    lateinit var idCardImage: MultipartBody.Part
    lateinit var idCardBackImage: MultipartBody.Part
    lateinit var phoneNumber:RequestBody
    lateinit var emergencyPhoneNumber: RequestBody
    lateinit var role:RequestBody
    lateinit var password:String

    fun getServicesApiCall(): MutableLiveData<ResponseBody> {

        val call = RetrofitClient.apiInterface.registerUser(
           profileImage, idCardImage, idCardBackImage, phoneNumber,
            emergencyPhoneNumber, role, password
        )

        call.enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.v("UserRepo---",t.message.toString())
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.v("UserRepo---",response.body().toString())
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

