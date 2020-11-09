package com.polyblack.contactsapp.repository

import android.content.Context
import com.polyblack.contactsapp.data.datasource.ContactDataSource
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.ui.fragments.contact_details.ContactNotificationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepository(context: Context) {
    private val dataSource = ContactDataSource(context)
    suspend fun getContactList(): List<ContactListItem.Item> =
        withContext(Dispatchers.IO) {
            dataSource.getContactList()
        }

    suspend fun getContactById(contactId: Int): ContactListItem.Item =
        withContext(Dispatchers.IO) {
            dataSource.getContactById(contactId)
        }

    fun changeContactNotificationStatus(contactItem: ContactListItem.Item) {
        ContactNotificationManager.changeNotificationState(contactItem)
    }
}
