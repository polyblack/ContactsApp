package com.polyblack.contactsapp.repository

import android.content.Context
import com.polyblack.contactsapp.data.datasource.ContactDataSource
import com.polyblack.contactsapp.data.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepository(context: Context) {
    private val dataSource = ContactDataSource(context)
    suspend fun getContactList(): List<Contact> =
        withContext(Dispatchers.IO) {
            dataSource.getContactList()
        }

    suspend fun getContactById(contactId: Int): Contact =
        withContext(Dispatchers.IO) {
            dataSource.getContactById(contactId)
        }
}
