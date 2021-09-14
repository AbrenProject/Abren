package com.example.abren.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Location
import com.example.abren.models.User
import com.example.abren.network.UserAPIClient
import com.example.abren.network.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//
//
//
class UserRepository {
    private var userService: UserService?=null

    init {
        userService = UserAPIClient.getApiClient().create(
            UserService::class.java)
    }
    fun login(phoneNumber:String,password:String):LiveData<User>{
        val data = MutableLiveData<User>()
        userService?.login(phoneNumber,password)?.enqueue(object : Callback<User> {

            override fun onFailure(call: Call<List<Location>>, t: Throwable) {
                data.value = object
            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {

                val res = response.body()
                if (response.code() == 200 && res!=null){
                    data.value = res
                }else{
                    data.value = object
                }
            }
        })
        return data
    }

    }
