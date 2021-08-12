package com.example.abren.network

import com.example.abren.models.PostModel
import retrofit2.Call
import retrofit2.http.GET

interface PostsService {
    @GET("posts")
    fun fetchAllPosts(): Call<List<PostModel>>
}