package com.example.abren.models

import java.util.*
import kotlin.collections.ArrayList

data class Route(
    var _id: String,
    var driverId: String,
    var startingLocation: Location,
    var waypointLocations: ArrayList<Location>,
    var destinationLocation: Location,
    var lastTaken: Date,
    var createdAt: Date,
    var updatedAt: Date,
    var deletedAt: Date
)