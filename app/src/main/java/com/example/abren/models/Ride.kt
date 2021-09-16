package com.example.abren.models

import java.time.LocalDateTime
import com.google.gson.annotations.Expose

data class Ride(@Expose val _id: String?,
                @Expose var driverId: String?,
                @Expose var route: Route?,
                @Expose var routeId: String?,
                @Expose var driverLocation: Location?,
                @Expose var status: String?,
                @Expose var requests: MutableList<String> = ArrayList(),
                @Expose var acceptedRequests: MutableList<String> = ArrayList(),
                var otp:Otp?,
                var createdAt: LocalDateTime? = LocalDateTime.now(),
                var updatedAt: LocalDateTime? = LocalDateTime.now(),
                var deletedAt: LocalDateTime? =null,)