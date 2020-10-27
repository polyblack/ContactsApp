package com.polyblack.contactsapp.ui

import com.polyblack.contactsapp.model.Contact

interface OnContactsListResultListener {
    fun onGetContactList(contactList: List<Contact>)
}
