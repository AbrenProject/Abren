package com.example.abren.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.abren.models.Location
import com.example.abren.models.Request
import com.example.abren.repository.RequestRepository
import com.example.abren.responses.RequestsResponse

class RequestViewModel(savedStateHandle: SavedStateHandle) : ViewModel()  {

    private var requestRepository: RequestRepository? = null
    private val mutableSelectedRequest = MutableLiveData<Request>()
    val selectedRequest: LiveData<Request> get() = mutableSelectedRequest

    var createdRequestLiveData: MutableLiveData<Request>? = null
    var acceptedRequestLiveData: MutableLiveData<Request>? = null

    var currentRequestedRider: MutableLiveData<Int>? = null
    var currentAccepted: MutableLiveData<Int>? = null

    var currentRequestsLiveData:  MutableLiveData<RequestsResponse>? = null

    init {
        requestRepository = RequestRepository()
        createdRequestLiveData = MutableLiveData()
        acceptedRequestLiveData = MutableLiveData()
        currentRequestedRider = MutableLiveData()
        currentRequestedRider?.value = 0
        currentAccepted = MutableLiveData()
        currentAccepted?.value = 0
        currentRequestsLiveData = MutableLiveData()
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

    fun fetchRequests(rideId: String, location: Location, context: Context) {
        currentRequestsLiveData = requestRepository?.fetchRequests(rideId, location, context)
    }

    fun startRide(requestId: String, otp: String, context: Context) {
        acceptedRequestLiveData = requestRepository?.startRide(requestId, otp, context)
    }

    fun setNextRequested() {
        currentRequestedRider?.value = currentRequestedRider?.value?.plus(1)
    }

    fun setPrevRequested() {
        currentRequestedRider?.value = currentRequestedRider?.value?.minus(1)
    }

    fun setNextAccepted() {
        currentAccepted?.value = currentAccepted?.value?.plus(1)
    }

    fun setPrevAccepted() {
        currentAccepted?.value = currentAccepted?.value?.minus(1)
    }
}