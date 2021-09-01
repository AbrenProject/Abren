package com.example.abren.models

import java.time.LocalDateTime
import com.squareup.moshi.Json

data class User(
    var _id: String? = null,
    @Json(name="name")
    var name: Name? = null,
    @Json(name = "phoneNumber")
    var phoneNumber: String? = null,
    var gender: String? = null,
    var ageGroup: String? = null,
    var password: String = "1234", //TODO: REMOVE
    var role: String? = null,
    var isVerified: Boolean = false,
    var emergencyPhoneNumber: String? = null,
    var profilePictureUrl: String? = null,
    var idCardUrl: String? = null,
    var idCardBackUrl: String? = null,
    var vehicleInformation: VehicleInformation? = VehicleInformation(),
    var preference: List<Preference>? = null,
    var rating: MutableList<Int> = MutableList(5) { 0 },
    var creditsBought: Double = 0.0,
    var creditsEarned: Double = 0.0,
    var createdAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null
)

//profilePicture
//idCardPicture
//idCardBackPicture
//data class UserResponse(@Json(name = "results") val result: List<User> ,
// @Json(name = "token") val token:token)
