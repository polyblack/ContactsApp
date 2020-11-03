package com.polyblack.contactsapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val id: Int,
    val name: String,
    var number: String? = null,
    var number2: String? = null,
    var email: String? = null,
    var email2: String? = null,
    val avatarUri: String? = null,
    val birthday: String? = null
) : Parcelable
