package com.example.abren.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.MenuItem
import com.example.abren.models.User
import com.example.abren.models.VehicleInformation
import com.example.abren.network.UserAPIClient
import com.example.abren.network.UserApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

//    private var userApiService: UserApiService?=null
//
//    init {
//        userApiService = UserAPIClient.getApiClient().create(UserApiService::class.java)
//
//    }
//
//    fun uploadImage(): ResponseBody{
//        Log.i("RETROFIT", "Calling in Repository")
//
//
//        //
//        val call: Call<ResponseBody> = UserApiService.uploadImage(null, body)
//        call.enqueue(object : Callback<ResponseBody?>() {
//            override fun onResponse(
//                call: Call<ResponseBody?>?,
//                response: Response<ResponseBody?>?
//            ) {
//                Log.v("Upload", "success")
//            }
//
//            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
//                t.message?.let { Log.e("Upload error:", it) }
//            }
//        })
//
//
//
//
//        //here
//        userApiService?.uploadImage()?.enqueue(object : Callback<User> {
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
////                data.value = String
//                Log.i("RETROFIT", t.message + "")
//
//            }
//
//            @SuppressLint("NullSafeMutableLiveData")
//            override fun onResponse(
//                call: Call<User>,
//                response: Response<VehicleInformation>
//            ) {
//
//                val res = response.body()
//                Log.i("RETROFIT", response.toString())
//                if (response.code() == 200 && res!=null){
////                    data.value = res.menuItem
//                }else{
////                    data.value = ArrayList()
//                }
//            }
//        })
//        return data
//    }

}