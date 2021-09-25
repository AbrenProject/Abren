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

class RequestViewModel(savedStateHandle: SavedStateHandle) : ViewModel()  {


    private var requestRepository: RequestRepository? = null
    private val mutableSelectedRequest = MutableLiveData<Request>()
    val selectedRequest: LiveData<Request> get() = mutableSelectedRequest
    var currentRequestedRides: MutableLiveData<Int>? = null
    var currentRequested: MutableLiveData<Int>? = null


    var createdRequestLiveData: MutableLiveData<Request>? = null
    var currentRequestedRidesLiveData: MutableLiveData<Request?>? = null

    init {
        requestRepository = RequestRepository()
        createdRequestLiveData = MutableLiveData()
        currentRequested = MutableLiveData()
        currentRequested?.value = 0
    }

    fun createRequest(request: Request, context: Context) {
        createdRequestLiveData = requestRepository?.createRequest(request, context)
    }
    fun getRequests(requestId: String, location: Location, context: Context) {
        currentRequestedRidesLiveData = requestRepository?.getRequests(requestId, location, context)
    }
    fun setNextRequested() {
        currentRequested?.value = currentRequested?.value?.plus(1)
    }

    fun setPrevRequested() {
        currentRequested?.value = currentRequested?.value?.minus(1)
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
}