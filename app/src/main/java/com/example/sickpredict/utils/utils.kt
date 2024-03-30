package com.example.sickpredict.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

object utils {


    fun getCurrentTimeStamp(): Long {
        return System.currentTimeMillis()
    }

    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return currentDateTime.format(formatter)
    }
}

fun getCurrentYear(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.YEAR)
}