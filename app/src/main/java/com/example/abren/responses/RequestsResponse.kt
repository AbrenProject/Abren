package com.example.abren.responses

import com.example.abren.models.Request

import com.google.gson.annotations.Expose

data class RequestsResponse (var requested: List<Request?>, var accepted: List<Request?>)