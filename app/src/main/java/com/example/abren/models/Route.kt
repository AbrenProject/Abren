package com.example.abren.models

import java.util.*
import kotlin.collections.ArrayList
import com.google.gson.annotations.Expose
import java.time.LocalDateTime

data class Route(
        @Expose var _id: String? = null,
        @Expose var driverId: String? = null,
        @Expose var startingLocation: Location? = null,
        @Expose var waypointLocations: ArrayList<Location> = ArrayList(),
        @Expose var destinationLocation: Location? = null,
        @Expose var lastTaken: Date? = null,
        var createdAt: LocalDateTime = LocalDateTime.now(),
        var updatedAt: LocalDateTime = LocalDateTime.now(),
        var deletedAt: LocalDateTime? = null
)