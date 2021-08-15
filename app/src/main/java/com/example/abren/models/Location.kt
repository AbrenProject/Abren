package com.example.abren.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @Expose @SerializedName(value = "display_name") var name: String,
    @Expose @SerializedName(value = "lat") var latitude: Double,
    @Expose @SerializedName(value = "lon") var longitude: Double
){
    override fun toString(): String {
        return name
    }
}
