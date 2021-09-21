package com.example.abren.responses

import com.example.abren.models.User
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AuthResponse (@Expose val user: User, @Expose val token: String)