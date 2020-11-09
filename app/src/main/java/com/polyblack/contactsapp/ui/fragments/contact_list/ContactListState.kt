package com.polyblack.contactsapp.ui.fragments.contact_list

import com.polyblack.contactsapp.data.model.ContactListItem

data class ContactListState(
    val isLoading: Boolean,
    val error: Throwable?,
    val data: List<ContactListItem>
)
