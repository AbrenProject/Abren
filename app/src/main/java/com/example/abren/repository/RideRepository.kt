package com.example.abren.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Location
import com.example.abren.models.Ride
import com.example.abren.models.Route
import com.example.abren.network.RetrofitClient
import com.example.abren.network.RideService
import com.example.abren.network.RouteService
import com.example.abren.responses.BadRequestResponse
import com.example.abren.responses.RidesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class RideRepository {
    private var rideService: RideService? = null
    private lateinit var prefs: SharedPreferences

    init {
        rideService = RetrofitClient.getApiClient().create(RideService::class.java)
    }

    private val nearbyData = MutableLiveData<RidesResponse?>()
    private val acceptedData = MutableLiveData<Ride?>()

    fun fetchNearbyRides(requestId: String, location: Location, context: Context): MutableLiveData<RidesResponse?> {
        Log.i("Ride Repo: Fetch Nearby", location.toString())

        prefs = context.getSharedPreferences("ABREN", Context.MODE_PRIVATE)

        rideService?.fetchNearbyRides(requestId, location, "Bearer ${prefs.getString("TOKEN", null)}")?.enqueue(object :
            Callback<RidesResponse> {
            override fun onFailure(call: Call<RidesResponse>, t: Throwable) {
                Log.d("Ride Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<RidesResponse>, response: Response<RidesResponse>) {
                val gson = Gson()
                if (response.code() == 200) {
                    nearbyData.value = response.body()
//                    Log.d("Ride Repo: On 200:", response.body().toString())
                }else {
                    nearbyData.value = null //TODO: Do for others too
                    Log.d("Ride Repo: Header = ", response.headers().toString())

                    if(response.code() != 401){
                        val errorResponse = gson.fromJson(response.errorBody()?.string(), BadRequestResponse::class.java)
                        if(errorResponse != null){
                            Log.d("Ride Repo: Error Body", errorResponse.message)
                        }else{
                            Log.d("Ride Repo: ", "Unknown Error")
                        }

                    }else{
                        Log.d("Ride Repo: Error - ", "Unauthorized")
                    }
                }
            }
        })

        return nearbyData
    }

    fun acceptRide(rideId: String, requestId: String, context: Context) : MutableLiveData<Ride?> {

        prefs = context.getSharedPreferences("ABREN", Context.MODE_PRIVATE)

        rideService?.acceptRide(rideId, requestId, "Bearer ${prefs.getString("TOKEN", null)}")?.enqueue(object :
            Callback<Ride> {

            override fun onFailure(call: Call<Ride>, t: Throwable) {
                Log.d("Ride Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<Ride>, response: Response<Ride>) {
                val gson = Gson()
                if (response.code() == 200) {
                    acceptedData.value = response.body()
//                    Log.d("Ride Repo: On 200:", response.body().toString())
                }else {
                    acceptedData.value = null //TODO: Do for others too
                    Log.d("Ride Repo: Header = ", response.headers().toString())

                    if(response.code() != 401){
                        val errorResponse = gson.fromJson(response.errorBody()?.string(), BadRequestResponse::class.java)
                        if(errorResponse != null){
                            Log.d("Ride Repo: Error Body", errorResponse.message)
                        }else{
                            Log.d("Ride Repo: ", "Unknown Error")
                        }

                    }else{
                        Log.d("Ride Repo: Error - ", "Unauthorized")
                    }
                }
            }
        })

        return acceptedData
    }





}