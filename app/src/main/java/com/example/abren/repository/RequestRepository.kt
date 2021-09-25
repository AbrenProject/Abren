package com.example.abren.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.Request
import com.example.abren.network.RequestService
import com.example.abren.network.RetrofitClient
import com.example.abren.responses.BadRequestResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class RequestRepository {
    private var requestService: RequestService?=null
    private lateinit var prefs: SharedPreferences

    init {
        requestService = RetrofitClient.getApiClient().create(RequestService::class.java)
    }

    private val data = MutableLiveData<Request>()

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

    fun getRequests(request: Request, context: Context) {

    }


}