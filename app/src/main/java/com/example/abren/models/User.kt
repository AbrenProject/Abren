package com.example.abren.models

import java.time.LocalDateTime
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose var _id: String? = null,
    @Expose var name: Name? = null,
    @Expose var phoneNumber: String? = null,
    @Expose var gender: String? = null,
    @Expose var ageGroup: String? = null,
    @Expose var password: String = "983479347568576374653", //TODO: REMOVE
    @Expose var role: String? = null,
    @Expose var isVerified: Boolean = false,
    @Expose var emergencyPhoneNumber: String? = null,
    @Expose var profilePictureUrl: String? = null,
    @Expose var idCardUrl: String? = null,
    @Expose var idCardBackUrl: String? = null,
    @Expose var vehicleInformation: VehicleInformation? = null,
    @Expose var preference: List<Preference>? = null,
    @Expose var rating: MutableList<Int> = MutableList(5) { 0 },
    @Expose var creditsBought: Double = 10000.0,
    @Expose var creditsEarned: Double = 0.0,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null
)
