package com.example.abren.models

import java.time.LocalDateTime
import com.google.gson.annotations.Expose

data class Ride(
    @Expose val _id: String? = null,
    @Expose var driverId: String? = null,
    @Expose var driverGender: String? = null,
    @Expose var driverAgeGroup: String? = null,
    @Expose var driverRating: MutableList<Int>? = MutableList(5) { 0 },
    @Expose var route: Route? = null,
    @Expose var routeId: String? = null,
    @Expose var driverLocation: Location? = null,
    @Expose var status: String? = null,
    @Expose var requests: MutableList<String> = ArrayList(),
    @Expose var acceptedRequests: MutableList<String> = ArrayList(),
    var otp: Otp? = null,
    var createdAt: LocalDateTime? = LocalDateTime.now(),
    var updatedAt: LocalDateTime? = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null,
)