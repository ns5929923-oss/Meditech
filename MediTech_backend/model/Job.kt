package com.example.meditechapp

data class Job(
    val jobId: Int = 0,
    val hospitalId: Int,
    val jobTitle: String,
    val specialization: String,
    val experienceRequired: Int,
    val salary: Double,
    val location: String,
    val jobDescription: String,
    val jobType: String,
    val postedDate: Long = System.currentTimeMillis()

)