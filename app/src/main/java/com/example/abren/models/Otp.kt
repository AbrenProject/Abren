package com.example.abren.models

import java.time.LocalDateTime
import com.google.gson.annotations.Expose

data class Otp(@Expose val code: String, @Expose val dateCreated: LocalDateTime, @Expose val isValidated: Boolean)