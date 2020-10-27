package com.polyblack.contactsapp.ui

interface ServiceCommandsListener {
    fun getContactList(listListener: OnContactsListResultListener)
    fun getContactById(contactId: Int)
}
