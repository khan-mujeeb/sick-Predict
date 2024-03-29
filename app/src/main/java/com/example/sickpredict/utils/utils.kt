package com.example.sickpredict.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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