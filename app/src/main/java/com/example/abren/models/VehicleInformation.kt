package com.example.abren.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VehicleInformation(
    @Expose var year: String? =  null,
    @Expose var make: String? = null,
    @Expose var model: String? = null,
    @Expose var licensePlateNumber: String? = null,
    @Expose var licenseUrl: String? = null,
    @Expose var ownershipDocUrl: String? = null,
    @Expose var insuranceDocUrl: String? = null,
    @Expose var vehiclePictureUrl: String? = null,
    @Expose var kml: Double? = null,
    @Expose var comb08: Double? = null,
    @Expose var menuItem: ArrayList<MenuItem>? = null
)

data class MenuItem(@Expose var text: String, @Expose var value: String){
    override fun toString(): String {
        return value
    }
}

data class MenuItemWrapper(@Expose var menuItem: MenuItem)