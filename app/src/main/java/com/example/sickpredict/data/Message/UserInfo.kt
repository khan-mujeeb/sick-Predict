package com.example.sickpredict.data.Message

data class UserInfo(
    val uid: String = "",
    val name: String = "",
    val phonenumber: String = "",
    val imgUri: String = "",
    val activeStatus: String = "",
    val about: String = "",
    val fcm_token: String = ""
)

