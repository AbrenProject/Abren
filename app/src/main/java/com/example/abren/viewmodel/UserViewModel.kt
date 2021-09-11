package com.example.abren.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.abren.models.User
import com.example.abren.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class UserViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private var userRepository: UserRepository?=null
    private val mutableSelectedItem = MutableLiveData<User>()
    val selectedUser: LiveData<User> get() = mutableSelectedItem

    //
    var servicesLiveData:MutableLiveData<ResponseBody>? = null
//    val phoneNumber:String = savedStateHandle["phoneNumber"]?:
//    throw IllegalArgumentException("missing phone no")


    fun selectUser(user: User) {
        mutableSelectedItem.value = user
    }

    fun setVehicleYear(year: String) {
        mutableSelectedItem.value?.vehicleInformation?.year = year
    }

    fun setPhoneNumber(phoneNumber: String) {
        mutableSelectedItem.value?.phoneNumber = phoneNumber.toString()
    }

    fun setEmergencyPhoneNumber(emergencyPhoneNumber: String) {
        mutableSelectedItem.value?.emergencyPhoneNumber = emergencyPhoneNumber.toString()
    }

    fun setIdCardPicture(idCardPicture: MultipartBody.Part?){
        mutableSelectedItem.value?.idCardUrl = idCardPicture.toString()
    }

    fun setIdCardBackPicture(idCardBackPicture: MultipartBody.Part?){
        mutableSelectedItem.value?.idCardBackUrl = idCardBackPicture.toString()
    }

    fun setProfilePicture(profilePicture: MultipartBody.Part?){
        mutableSelectedItem.value?.profilePictureUrl = profilePicture.toString()
    }

    fun setPasssword(){
        mutableSelectedItem.value?.password = UserRepository.getPasssword()
    }

    fun registerUser(profileImage:MultipartBody.Part,
                     idCardImage:MultipartBody.Part,
                     idCardBackImage:MultipartBody.Part,
                     phoneNumber: RequestBody,
                     emergencyPhoneNumber: RequestBody,
                     role: RequestBody,
                     password:RequestBody):LiveData<ResponseBody>? {
        Log.i("RETROFIT:USER VIEWMODEL IN REGISTERUSER FUNCtion" , selectedUser.value.toString())
        servicesLiveData = UserRepository.getServicesApiCall(profileImage,
            idCardImage,
            idCardBackImage,
            phoneNumber,
            emergencyPhoneNumber,
            role,
            password)
        return servicesLiveData
    }


}