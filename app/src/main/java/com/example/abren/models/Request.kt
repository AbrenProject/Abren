package com.example.abren.models

import java.time.LocalDateTime
import com.google.gson.annotations.Expose

data class Request(
    @Expose var _id: String? = null,
    @Expose var riderId: String? = null,
    @Expose var riderGender: String? = null,
    @Expose var riderAgeGroup: String? = null,
    @Expose var riderRating: MutableList<Int>? = null,
    @Expose var riderLocation: Location? = null,
    @Expose var destination: Location? = null,
    @Expose var status: String? = null,
    @Expose var requestedRides: MutableList<String>? = ArrayList(),
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var deletedAt: LocalDateTime? = null
)

