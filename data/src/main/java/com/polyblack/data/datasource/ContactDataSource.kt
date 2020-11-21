package com.polyblack.data.datasource

import com.polyblack.domain.entities.Contact

interface ContactDataSource {
    fun getContactList(): List<Contact>
    fun getContactById(contactId: Int): Contact
}
