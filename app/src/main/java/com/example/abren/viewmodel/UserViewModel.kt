package com.example.abren.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abren.models.User

class UserViewModel : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<User>()
    val selectedUser: LiveData<User> get() = mutableSelectedItem

    fun selectUser(user: User) {
        mutableSelectedItem.value = user
    }

    fun setPhoneNumber(phoneNumber: String) {
        mutableSelectedItem.value?.phoneNumber = phoneNumber
    }

    fun setEmergencyPhoneNumber(emergencyPhoneNumber: String) {
        mutableSelectedItem.value?.emergencyPhoneNumber = emergencyPhoneNumber
    }

    fun setVehicleYear(year: String) {
        mutableSelectedItem.value?.vehicleInformation?.year = year
    }

    fun setRating(ratings: MutableList<Int>){
        mutableSelectedItem.value?.rating = ratings
    }
//
//    fun makeApiCalls(){
//        viewModelScope.launch(Dispatchers.IO ){
//            val userProfileClient = UserProfileClient.getApiClient().create(UserProfileService::class.java)
//            val response = userProfileClient.getDataFromApi()
//        }
//    }
    //TODO: Implement remaining setters
}