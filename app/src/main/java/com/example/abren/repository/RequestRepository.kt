package com.example.abren.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Location
import com.example.abren.models.Request
import com.example.abren.models.Ride
import com.example.abren.network.RequestService
import com.example.abren.network.MainAPIClient
import com.example.abren.responses.BadRequestResponse
import com.example.abren.responses.RequestsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class RequestRepository {
    private var requestService: RequestService?=null
    private lateinit var prefs: SharedPreferences

    init {
        requestService = MainAPIClient.getApiClient().create(RequestService::class.java)
    }

    private val data = MutableLiveData<Request>()
    private val acceptedRequestData = MutableLiveData<Request>()
    private val rideData = MutableLiveData<Ride>()
    private val requestListData = MutableLiveData<RequestsResponse>()

    fun createRequest(request: Request, context: Context): MutableLiveData<Request> {
        Log.i("Request Repo: Create Request" , request.toString())

        prefs = context.getSharedPreferences("ABREN", Context.MODE_PRIVATE)

        requestService?.createRequest(request, "Bearer ${prefs.getString("TOKEN", null)}")?.enqueue(object : Callback<Request> {
            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.d("Request Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                val gson = Gson()
                if (response.code() == 200) {
                    data.value = response.body()
                    Log.d("Request Repo: On 200:", response.body().toString())
                }else {
                    Log.d("Request Repo: Header = ", response.headers().toString())

                    if(response.code() != 401){
                        val errorResponse = gson.fromJson(response.errorBody()?.string(), BadRequestResponse::class.java)
                        Log.d("Request Repo: Error Body", errorResponse.message)
                    }else{
                        Log.d("Request Repo: Error - ", "Unauthorized")
                    }
                }
            }
        })

        return data
    }

    fun sendRequest(requestId: String, rideId: String, context: Context): MutableLiveData<Ride> {
        Log.i("Request Repo: Send Request" , requestId)

        prefs = context.getSharedPreferences("ABREN", Context.MODE_PRIVATE)

        requestService?.sendRequest(requestId, rideId, "Bearer ${prefs.getString("TOKEN", null)}")?.enqueue(object : Callback<Ride> {
            override fun onFailure(call: Call<Ride>, t: Throwable) {
                Log.d("Request Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<Ride>, response: Response<Ride>) {
                val gson = Gson()
                if (response.code() == 200) {
                    rideData.value = response.body()
                    Log.d("Request Repo: On 200:", response.body().toString())
                }else {
                    Log.d("Request Repo: Header = ", response.headers().toString())

                    if(response.code() != 401){
                        val errorResponse = gson.fromJson(response.errorBody()?.string(), BadRequestResponse::class.java)
                        Log.d("Request Repo: Error Body", errorResponse.message)
                    }else{
                        Log.d("Request Repo: Error - ", "Unauthorized")
                    }
                }
            }
        })

        return rideData
    }

    fun startRide(requestId: String, otp: String, context: Context): MutableLiveData<Request> {
        Log.i("Request Repo: Start Ride" , requestId)

        prefs = context.getSharedPreferences("ABREN", Context.MODE_PRIVATE)

        requestService?.startRide(requestId, otp, "Bearer ${prefs.getString("TOKEN", null)}")?.enqueue(object : Callback<Request> {
            override fun onFailure(call: Call<Request>, t: Throwable) {
                Log.d("Request Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<Request>, response: Response<Request>) {
                val gson = Gson()
                if (response.code() == 200) {
                    acceptedRequestData.value = response.body()
                    Log.d("Request Repo: On 200:", response.body().toString())
                }else {
                    Log.d("Request Repo: Header = ", response.headers().toString())

                    if(response.code() != 401){
                        val errorResponse = gson.fromJson(response.errorBody()?.string(), BadRequestResponse::class.java)
                        Log.d("Request Repo: Error Body", errorResponse.message)
                    }else{
                        Log.d("Request Repo: Error - ", "Unauthorized")
                    }
                }
            }
        })

        return acceptedRequestData
    }

    fun fetchRequests(rideId: String, location: Location, context: Context): MutableLiveData<RequestsResponse> {
        Log.i("Request Repo: Get Requests" , rideId)

        prefs = context.getSharedPreferences("ABREN", Context.MODE_PRIVATE)

        requestService?.fetchRequests(rideId, location, "Bearer ${prefs.getString("TOKEN", null)}")?.enqueue(object : Callback<RequestsResponse> {
            override fun onFailure(call: Call<RequestsResponse>, t: Throwable) {
                Log.d("Request Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<RequestsResponse>, response: Response<RequestsResponse>) {
                val gson = Gson()
                if (response.code() == 200) {
                    requestListData.value = response.body()
                    Log.d("Request Repo: On 200:", response.body().toString())
                }else {
                    Log.d("Request Repo: Header = ", response.headers().toString())

                    if(response.code() != 401){
                        val errorResponse = gson.fromJson(response.errorBody()?.string(), BadRequestResponse::class.java)
                        Log.d("Request Repo: Error Body", errorResponse.message)
                    }else{
                        Log.d("Request Repo: Error - ", "Unauthorized")
                    }
                }
            }
        })

        return requestListData
    }
}