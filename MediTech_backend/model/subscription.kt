package com.example.meditechapp.model

class subscription {
    val subscriptionId: Int = 0,
    val doctorId: Int,
    val planName: String,
    val price: Double,
    val startDate: Long = System.currentTimeMillis(),
    val endDate: Long,
    val paymentStatus: String

}