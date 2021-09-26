package com.example.abren.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.abren.responses.BadRequestResponse
import com.example.abren.models.User
import com.example.abren.repository.UserRepository
import com.example.abren.responses.AuthResponse

class UserViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private var userRepository: UserRepository? = null
    private val mutableSelectedItem = MutableLiveData<User>()
    val selectedUser: LiveData<User> get() = mutableSelectedItem
    var registeredUserLiveData : MutableLiveData<AuthResponse>? = null
    var storedVerificationId: String? = null
    var loginPhoneNumber: String? = null

    init {
        userRepository = UserRepository()
        registeredUserLiveData = MutableLiveData()
    }

    fun registerUser(user: User) {
        registeredUserLiveData = userRepository?.registerUser(user)
    }

    fun login(user: User) {
        registeredUserLiveData = userRepository?.login(user)
    }

    fun rate(id: String, rating: String, context: Context) {
        userRepository?.rate(id, rating, context)
    }

    fun selectUser(user: User) {
        mutableSelectedItem.value = user
    }

    fun setPhoneNumber(phoneNumber: String) {
        mutableSelectedItem.value?.phoneNumber = phoneNumber
    }

    fun setEmergencyPhoneNumber(emergencyPhoneNumber: String) {
        mutableSelectedItem.value?.emergencyPhoneNumber = emergencyPhoneNumber
    }

    fun setIdCardUrl(idCardUrl: String){
        mutableSelectedItem.value?.idCardUrl = idCardUrl
    }

    fun setIdCardBackUrl(idCardBackUrl: String){
        mutableSelectedItem.value?.idCardBackUrl = idCardBackUrl
    }

    fun setProfilePictureUrl(profilePictureUrl: String){
        mutableSelectedItem.value?.profilePictureUrl = profilePictureUrl
    }

    fun setPassword(password: String){
        mutableSelectedItem.value?.password = password
    }

    fun setVehicleYear(year: String) {
        mutableSelectedItem.value?.vehicleInformation?.year = year
    }

    fun setVehicleMake(make: String) {
        mutableSelectedItem.value?.vehicleInformation?.make = make
    }

    fun setVehicleModel(model: String) {
        mutableSelectedItem.value?.vehicleInformation?.model = model
    }

    fun setKml(kml: Double) {
        mutableSelectedItem.value?.vehicleInformation?.kml = kml
    }

    fun setVehicleLicensePlateNumber(licensePlateNumber: String) {
        mutableSelectedItem.value?.vehicleInformation?.licensePlateNumber = licensePlateNumber
    }

    fun setLicenseUrl(licenseUrl: String) {
        mutableSelectedItem.value?.vehicleInformation?.licenseUrl = licenseUrl
    }

    fun setOwnershipDocUrl(ownershipDocUrl: String) {
        mutableSelectedItem.value?.vehicleInformation?.ownershipDocUrl = ownershipDocUrl
    }

    fun setInsuranceDocUrl(insuranceDocUrl: String) {
        mutableSelectedItem.value?.vehicleInformation?.insuranceDocUrl = insuranceDocUrl
    }

    fun setVehiclePictureUrl(vehiclePictureUrl: String) {
        mutableSelectedItem.value?.vehicleInformation?.vehiclePictureUrl = vehiclePictureUrl
    }
}