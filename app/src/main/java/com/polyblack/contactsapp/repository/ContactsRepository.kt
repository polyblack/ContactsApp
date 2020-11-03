package com.polyblack.contactsapp.repository

import android.content.ContentUris
import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import com.polyblack.contactsapp.model.Contact


class ContactsRepository {

    companion object {
        private val contactList1 = arrayListOf(
            Contact(0, "name 0", "number 0", email = "email 0", birthday = "04-01"),
            Contact(1, "name 1", "number 1", email = "email 1", birthday = "04-02"),
            Contact(2, "name 2", "number 2", email = "email 2", birthday = "04-03"),
            Contact(3, "name 3", "number 3", email = "email 3", birthday = "04-04"),
            Contact(4, "name 4", "number 4", email = "email 4", birthday = "04-05"),
            Contact(5, "name 5", "number 5", email = "email 5", birthday = "04-06"),
        )

        fun getContactList(context: Context): ArrayList<Contact>? {
            val cursor = context.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null,
                null, null
            )
            val contactList = ArrayList<Contact>()
            if ((cursor?.count ?: 0) > 0) {
                while (cursor != null && cursor.moveToNext()) {
                    val id: Int = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts._ID)
                    ).toInt()
                            )
                    val name: String = cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )
                    var number: String? = null
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) number =
                        getPhoneNumbers(context, id)[0]
                    contactList.add(Contact(id, name, number))
                }
            }
            cursor?.close()
            return contactList
        }


        fun getContactById(context: Context, contactId: Int): Contact? {
            var contact: Contact? = null
            context.contentResolver.query(
                ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    contactId.toLong()
                ), null, null, null, null
            )?.let { cursor ->
                contact = Contact(
                    contactId, cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )
                )
                val numbers = ArrayList<String>()
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    numbers.addAll(
                        getPhoneNumbers(
                            context,
                            contactId
                        )
                    )
                    if (numbers.isNotEmpty()) {
                        contact!!.number = numbers[0]
                        numbers[1].run { contact!!.number2 = this }
                    }
                }
                val emailsCursor = context.contentResolver.query(
                    Email.CONTENT_URI,
                    null,
                    Email.CONTACT_ID + " = ?",
                    arrayOf(contactId.toString()),
                    null
                )
                val counter = 0
                while (emailsCursor != null && emailsCursor.moveToNext()) {
                    when (counter) {
                        0 -> {
                            contact!!.email =
                                emailsCursor.getString(emailsCursor.getColumnIndex(Email.DATA))
                            counter.inc()
                        }
                        1 -> {
                            contact!!.email2 =
                                emailsCursor.getString(emailsCursor.getColumnIndex(Email.DATA))
                            break
                        }
                    }
                    break
                }
                emailsCursor?.close()
            }
            return contact
        }

        private fun getPhoneNumbers(context: Context, contactId: Int): List<String> {
            val numbers = mutableListOf<String>()
            val phoneCursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(contactId.toString()),
                null
            )
            while (phoneCursor != null && phoneCursor.moveToNext()) {
                numbers.add(
                    phoneCursor.getString(
                        phoneCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER
                        )
                    )
                )
            }
            phoneCursor?.close()
            return numbers
        }
    }
}
