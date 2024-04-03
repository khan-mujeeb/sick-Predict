package com.example.sickpredict.data.prediction

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.sql.Timestamp

data class PreductionResult(
    val timestamp: String = "",
    val prediction: String = "",
    val accuracy: String = "",
    val symtomps: List<String> = listOf(),
    val medcines: List<String> = listOf()
): Serializable
