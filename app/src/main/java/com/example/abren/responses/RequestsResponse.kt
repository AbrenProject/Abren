package com.example.abren.responses

import com.example.abren.models.Request

import com.google.gson.annotations.Expose

data class RequestsResponse (@Expose var requested: List<Request?>, @Expose var accepted: List<Request?>)