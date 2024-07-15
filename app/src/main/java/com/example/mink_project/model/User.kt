package com.example.mink_project.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val name: String = "",
    val phoneNumber: String = "",
    val city: String = "",
    val description: String = "",
    val preferences: List<String> = emptyList(),
    val profileImageUrl: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: emptyList(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
        parcel.writeString(city)
        parcel.writeString(description)
        parcel.writeStringList(preferences)
        parcel.writeString(profileImageUrl)
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
