package com.example.abren.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.abren.responses.BadRequestResponse
import com.example.abren.models.User
import com.example.abren.network.MainAPIClient
import com.example.abren.network.UserService
import com.example.abren.responses.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class UserRepository {
    private var userService: UserService? = null
    private lateinit var prefs: SharedPreferences

    init {
        userService = MainAPIClient.getApiClient().create(UserService::class.java)
    }

    private val data = MutableLiveData<AuthResponse>()
    private val userData = MutableLiveData<User>()

    fun registerUser(user: User): MutableLiveData<AuthResponse> {
        Log.i("User Repo: Register User", user.toString())

        userService?.registerUser(user)?.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("User Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val gson = Gson()
                Log.i("User Repo: ", response.toString())
                Log.i("User Repo - Request: ", gson.toJson(call.request().body))
                if (response.code() == 200) {
                    data.value = response.body()
                    Log.d("User Repo: On 200:", response.body().toString())
                } else {
                    Log.d("User Repo: Header = ", response.headers().toString())
                    Log.d("User Repo: Body", response.errorBody().toString())


                    val errorResponse = gson.fromJson(
                        response.errorBody()?.string(),
                        BadRequestResponse::class.java
                    )
                    Log.d("User Repo: Error Body", errorResponse.message)
                }
            }
        })

        return data
    }

    fun login(user: User): MutableLiveData<AuthResponse> {
        Log.i("User Repo: Login User", user.toString())

        userService?.login(user)?.enqueue(object : Callback<AuthResponse> {
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("User Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                val gson = Gson()
                Log.i("User Repo: ", response.toString())
                Log.i("User Repo - Request: ", gson.toJson(call.request().body))
                if (response.code() == 200) {
                    data.value = response.body()
                    Log.d("User Repo: On 200:", response.body().toString())
                } else {
                    Log.d("User Repo: Header = ", response.headers().toString())
                    Log.d("User Repo: Body", response.errorBody().toString())


                    val errorResponse = gson.fromJson(
                        response.errorBody()?.string(),
                        BadRequestResponse::class.java
                    )
                    Log.d("User Repo: Error Body", errorResponse.message)
                }
            }
        })

        return data
    }

    fun rate(id: String, rating: String, context: Context): MutableLiveData<User> {
        Log.i("User Repo: Rate User", id)

        prefs = context.getSharedPreferences("ABREN", Context.MODE_PRIVATE)

        userService?.rate(id, rating, "Bearer ${prefs.getString("TOKEN", null)}")?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("User Repo: ONFailure: ", t.message.toString())
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val gson = Gson()
                Log.i("User Repo: ", response.toString())
                Log.i("User Repo - Request: ", gson.toJson(call.request().body))
                if (response.code() == 200) {
                    userData.value = response.body()
                    Log.d("User Repo: On 200:", response.body().toString())
                } else {
                    Log.d("User Repo: Header = ", response.headers().toString())
                    Log.d("User Repo: Body", response.errorBody().toString())


                    val errorResponse = gson.fromJson(
                        response.errorBody()?.string(),
                        BadRequestResponse::class.java
                    )
                    Log.d("User Repo: Error Body", errorResponse.message)
                }
            }
        })

        return userData
    }

}

