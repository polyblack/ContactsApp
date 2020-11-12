package com.polyblack.contactsapp.data.model

data class Contact(
    val id: Int,
    val name: String,
    val number: String? = null,
    val number2: String? = null,
    val email: String? = null,
    val email2: String? = null,
    val avatarUri: String? = null,
    val birthday: String? = null,
    var isNotificationOn: Boolean? = null
)
