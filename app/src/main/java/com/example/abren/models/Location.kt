package com.example.abren.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Location(
    @Expose @SerializedName(value = "display_name") var displayName: String? = null,
    @Expose var lat: Double? = null,
    @Expose var lon: Double? = null,

    @Expose var name: String? = null,
    @Expose var latitude: Double? = null,
    @Expose var longitude: Double? = null
){
    override fun toString(): String {
        return if(displayName != null){
            displayName!!
        }else{
            super.toString()
        }
    }
}
