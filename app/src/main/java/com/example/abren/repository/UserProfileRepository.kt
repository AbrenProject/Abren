package com.example.abren.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Location
import com.example.abren.models.User
import com.example.abren.network.LocationAPIClient
import com.example.abren.network.LocationService
import com.example.abren.network.UserProfileAPIClient
import com.example.abren.network.UserProfileService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserProfileRepository {

    private var userProfileService: UserProfileService?=null

    init {
        userProfileService = UserProfileAPIClient.getApiClient().create(UserProfileService::class.java)
    }






}