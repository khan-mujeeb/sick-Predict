package com.example.sickpredict.data

data class PatientPriscription (
    val disease: String = "",
    val medicines: List<String> = emptyList()
)