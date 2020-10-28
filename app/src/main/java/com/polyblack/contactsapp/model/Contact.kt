package com.polyblack.contactsapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val name: String,
    val number: String,
    val number2: String? = null,
    val email: String? = null,
    val email2: String? = null,
    val avatarUri: String? = null
) : Parcelable
