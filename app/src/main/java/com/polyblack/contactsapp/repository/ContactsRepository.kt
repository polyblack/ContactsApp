package com.polyblack.contactsapp.repository

import com.polyblack.contactsapp.model.Contact

class ContactsRepository {

    companion object {
        val contactList = arrayListOf(
            Contact(0, "name 0", "number 0", email = "email 0", birthday = "04-01"),
            Contact(1, "name 1", "number 1", email = "email 1", birthday = "04-02"),
            Contact(2, "name 2", "number 2", email = "email 2", birthday = "04-03"),
            Contact(3, "name 3", "number 3", email = "email 3", birthday = "04-04"),
            Contact(4, "name 4", "number 4", email = "email 4", birthday = "04-05"),
            Contact(5, "name 5", "number 5", email = "email 5", birthday = "04-06"),
        )

        fun getContactById(contactId: Int): Contact = contactList[contactId]
    }
}
