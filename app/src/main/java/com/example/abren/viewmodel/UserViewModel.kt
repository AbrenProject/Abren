package com.example.abren.viewmodel

import android.graphics.Picture
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abren.models.User
import com.example.abren.repository.UserRepository

class UserViewModel : ViewModel() {

    private var userRepository: UserRepository?=null
    private val mutableSelectedItem = MutableLiveData<User>()
    val selectedUser: LiveData<User> get() = mutableSelectedItem

    fun selectUser(user: User) {
        mutableSelectedItem.value = user
    }

    fun setVehicleYear(year: String) {
        mutableSelectedItem.value?.vehicleInformation?.year = year
    }

    fun setPhoneNumber(phoneNumber: String) {
        mutableSelectedItem.value?.phoneNumber = phoneNumber
    }

    fun setEmergencyPhoneNumber(emergencyPhoneNumber: String) {
        mutableSelectedItem.value?.emergencyPhoneNumber = emergencyPhoneNumber
    }

    fun setIdCardPicture(idCardPicture: String){
        mutableSelectedItem.value?.idCardUrl = idCardPicture
    }

    fun setIdCardBackPicture(idCardBackPicture: String){
        mutableSelectedItem.value?.idCardBackUrl = idCardBackPicture
    }

    fun setProfilePicture(profilePicture: String){
        mutableSelectedItem.value?.profilePictureUrl = profilePicture
    }

    fun registerUser(){
        Log.i("RETROFIT:IN USER VIEWMODEL" , "in user viewmodel")
        mutableSelectedItem.value
    }



}