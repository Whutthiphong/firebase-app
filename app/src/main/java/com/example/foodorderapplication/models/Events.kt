package com.example.foodorderapplication.models

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Events(
    val eventId: String,
    val userId: String,
    val title: String,
    val description: String
) {
constructor ():this("","","","")
}