package com.example.sickpredict.data.user

import android.os.Parcel
import android.os.Parcelable

data class User (
    val fullname: String = "",
    val email: String = "",
    val dob: String = "",
    val gender: String =  "",
    val mobile: String = "",
    var profile: String = "",
    val uid: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullname)
        parcel.writeString(email)
        parcel.writeString(dob)
        parcel.writeString(gender)
        parcel.writeString(mobile)
        parcel.writeString(profile)
        parcel.writeString(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
