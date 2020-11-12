package com.polyblack.contactsapp.data.model

sealed class ContactListItem {
    data class Error(val error: Throwable) : ContactListItem()

    data class Item(
        val contact: Contact
    ) : ContactListItem()

    object Loading : ContactListItem()
}
