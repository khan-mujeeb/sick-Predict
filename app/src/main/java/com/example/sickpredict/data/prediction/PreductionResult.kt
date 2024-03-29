package com.example.sickpredict.data.prediction

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class PreductionResult(
    val prediction: String = "",
    val accuracy: String = "",
    val symtomps: List<String> = listOf(),
    val medcines: ArrayList<String> = ArrayList()
): Serializable
