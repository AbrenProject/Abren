package com.example.abren.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.abren.models.PostModel
import com.example.abren.repository.PostRepository

class PostViewModel(application: Application): AndroidViewModel(application) {

    private var postRepository:PostRepository?=null
    var postModelListLiveData : LiveData<List<PostModel>>?=null


    init {
        postRepository = PostRepository()
        postModelListLiveData = MutableLiveData()
    }

    fun fetchAllPosts(){
        postModelListLiveData = postRepository?.fetchAllPosts()
    }
}