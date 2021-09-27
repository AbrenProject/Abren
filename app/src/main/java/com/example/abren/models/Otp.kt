package com.example.abren.models

import java.time.LocalDateTime
import com.google.gson.annotations.Expose

data class Otp(@Expose val code: String, val dateCreated: LocalDateTime, @Expose val isValidated: Boolean)