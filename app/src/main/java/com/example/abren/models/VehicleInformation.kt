package com.example.abren.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VehicleInformation(
    var year: String? =  null,
    var make: String? = null,
    var model: String? = null,
    var licensePlateNumber: String? = null,
    var licenseUrl: String? = null,
    var ownershipDocUrl: String? = null,
    var insuranceDocUrl: String? = null,
    var vehiclePictureUrl: String? = null,
    var kml: Double? = null,
    @Expose var menuItem: ArrayList<MenuItem>? = null
)

data class MenuItem(@Expose var text: String, @Expose var value: String){
    override fun toString(): String {
        return value
    }
}