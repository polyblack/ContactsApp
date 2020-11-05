package com.polyblack.contactsapp.repository

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import com.polyblack.contactsapp.model.Contact

class ContactsRepository(private val context: Context) {

    fun getContactList(): ArrayList<Contact>? {
        val contactList = ArrayList<Contact>()
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null,
            null, null
        ).use { cursor ->
            while (cursor != null && cursor.moveToNext()) {
                val id: Int = (cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                    )
                ).toInt())
                val name: String = cursor.getString(
                    cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                var number: String? = null
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) number =
                    getPhoneNumbers(id)[0]
                val avatarUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                contactList.add(Contact(id, name, number, avatarUri = avatarUri))
            }
        }
        return contactList
    }

    fun getContactById(contactId: Int): Contact? {
        lateinit var contact: Contact
        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            ContactsContract.Contacts._ID + " = ?",
            arrayOf(contactId.toString()),
            null,
            null
        ).use { contactCursor ->
            if (contactCursor != null && contactCursor.moveToNext()) {
                var number1: String? = null
                var number2: String? = null
                var email1: String? = null
                var email2: String? = null
                val name =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val avatarUri: String? =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                val numbers = ArrayList<String>()
                if (contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    numbers.addAll(
                        getPhoneNumbers(contactId)
                    )
                    number1 = numbers.firstOrNull()
                    number2 = numbers.getOrNull(1)
                }
                val emails = ArrayList<String>()
                getIfContactHasEmailsResult(contactId)?.let {
                    emails.addAll(getEmails(it))
                    email1 = emails.firstOrNull()
                    email2 = emails.getOrNull(1)
                }
                contact = Contact(contactId, name, number1, number2, email1, email2, avatarUri)
            }
        }
        return contact
    }

    private fun getPhoneNumbers(contactId: Int): List<String> {
        val numbers = mutableListOf<String>()
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        ).use { phoneCursor ->
            while (phoneCursor != null && phoneCursor.moveToNext()) {
                numbers.add(
                    phoneCursor.getString(
                        phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                    )
                )
            }
        }
        return numbers
    }

    private fun getIfContactHasEmailsResult(contactId: Int): Cursor? =
        context.contentResolver.query(
            Email.CONTENT_URI,
            null,
            Email.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        ).use { it }

    private fun getEmails(emailsCursor: Cursor): List<String> {
        val emails = mutableListOf<String>()
        emailsCursor.use {
            while (emailsCursor.moveToNext()) {
                emails.add(emailsCursor.getString(emailsCursor.getColumnIndex(Email.DATA)))
            }
        }
        return emails
    }
}
