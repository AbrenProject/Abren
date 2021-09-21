package com.example.abren.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BadRequestResponse(@Expose val message: String)
