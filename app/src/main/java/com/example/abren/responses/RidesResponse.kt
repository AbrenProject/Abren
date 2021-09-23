package com.example.abren.responses

import com.example.abren.models.Ride
import com.google.gson.annotations.Expose

data class RidesResponse(
    @Expose var requested: List<Ride?>,
    @Expose var nearby: List<Ride?>,
    @Expose var accepted: Ride?
)