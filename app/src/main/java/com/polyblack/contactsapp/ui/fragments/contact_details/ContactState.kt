package com.polyblack.contactsapp.ui.fragments.contact_details

import com.polyblack.contactsapp.data.model.ContactListItem

data class ContactState(val isLoading: Boolean, val error: Throwable?, val data: ContactListItem)
