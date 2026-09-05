package com.example.meditechapp.model

data class Doctor(
    val doctorId: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val phone: String = "",
    val specialization: String,
    val experience: Int = 0,
    val qualification: String = "",
    val location: String = "",
    val licenseNumber: String = "",
    val subscriptionStatus: String = "Free",
    val createdAt: Long = System.currentTimeMillis()
)