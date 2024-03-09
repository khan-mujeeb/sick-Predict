package com.example.sickpredict.data.doctor

data class Doctor(
    val name: String,
    val qualification: String,
    val experience: String,
    val rating: String,
    val fees: String,
    val specalization: ArrayList<String>,
    val imageUrl: String,
    val uid: String
)
