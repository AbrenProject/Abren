package com.example.abren.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.PostModel
import com.example.abren.network.APIClient
import com.example.abren.network.PostsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepository {
    private var postsService:PostsService?=null

    init {
        postsService = APIClient.getApiClient().create(PostsService::class.java)
    }

    fun fetchAllPosts(): LiveData<List<PostModel>> {
        val data = MutableLiveData<List<PostModel>>()

        postsService?.fetchAllPosts()?.enqueue(object : Callback<List<PostModel>> {

            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                data.value = ArrayList()
            }

            @SuppressLint("NullSafeMutableLiveData")
            override fun onResponse(
                call: Call<List<PostModel>>,
                response: Response<List<PostModel>>
            ) {

                val res = response.body()
                if (response.code() == 200 && res!=null){
                    data.value = res
                }else{
                    data.value = ArrayList()
                }
            }
        })
        return data
    }
}