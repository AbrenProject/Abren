package com.example.abren.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.abren.models.Location
import com.example.abren.models.Request
import com.example.abren.models.User
import com.example.abren.repository.RequestRepository
import com.example.abren.responses.RequestsResponse
import com.example.abren.responses.RidesResponse

class RequestViewModel(savedStateHandle: SavedStateHandle) : ViewModel()  {

    private var requestRepository: RequestRepository? = null
    private val mutableSelectedRequest = MutableLiveData<Request>()
    val selectedRequest: LiveData<Request> get() = mutableSelectedRequest
    var currentRequested: MutableLiveData<Int>? = null
    var currentRidersRequest: MutableLiveData<Int>? =null
    var createdRequestLiveData: MutableLiveData<Request>? = null
    var acceptedRequestLiveData: MutableLiveData<Request>? = null
    var requestedLiveData:  MutableLiveData<RequestsResponse?>? = null

    init {
        requestRepository = RequestRepository()
        createdRequestLiveData = MutableLiveData()
        acceptedRequestLiveData = MutableLiveData()
        currentRequested = MutableLiveData()
        currentRequested?.value = 0
        requestedLiveData = MutableLiveData()
        currentRidersRequest = MutableLiveData()

    }

    fun setRequest(request: Request) {
        mutableSelectedRequest.value = request
    }

    fun setRiderLocation(location: Location) {
        mutableSelectedRequest.value?.riderLocation = location
    }

    fun setDestination(location: Location) {
        mutableSelectedRequest.value?.destination = location
    }

    fun createRequest(request: Request, context: Context) {
        createdRequestLiveData = requestRepository?.createRequest(request, context)
    }

    fun sendRequest(requestId: String, rideId: String, context: Context) {
        requestRepository?.sendRequest(requestId, rideId, context)
    }

    fun getRequests(requestId: String, rideId: String, context: Context) {
       requestedLiveData = requestRepository?.getRequests(requestId, rideId, context)
    }

    fun startRide(requestId: String, otp: String, context: Context) {
        acceptedRequestLiveData = requestRepository?.startRide(requestId, otp, context)
    }

    fun setNextRequested() {
        currentRequested?.value = currentRequested?.value?.plus(1)
    }

    fun setPrevRequested() {
        currentRequested?.value = currentRequested?.value?.minus(1)
    }
}