package com.polyblack.contactsapp.repository

import com.polyblack.contactsapp.model.Contact

class ContactsRepository {

    companion object {
        fun getContactList(): ArrayList<Contact> {
            val contactList = ArrayList<Contact>()
            for (i in 0..5) {
                contactList.add(Contact("name $i", "number $i", email = "email $i"))
            }
            return contactList
        }

        fun getContactById(contactId: Int): Contact {
            val contactList = mutableListOf<Contact>()
            for (i in 0..5) {
                contactList.add(Contact("name $i", "number $i", email = "email $i"))
            }
            return contactList[contactId]
        }
    }
}
