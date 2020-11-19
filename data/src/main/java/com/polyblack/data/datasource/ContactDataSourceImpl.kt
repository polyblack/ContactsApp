package com.polyblack.data.datasource

import android.content.Context
import android.provider.ContactsContract
import com.polyblack.domain.entities.Contact
import javax.inject.Inject

class ContactDataSourceImpl @Inject constructor(
    val context: Context
) : ContactDataSource {
    override fun getContactList(): List<Contact> {
        val contactList = mutableListOf<Contact>()
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
                contactList.add(
                    Contact(
                        id,
                        name,
                        number,
                        avatarUri = avatarUri
                    )
                )
            }
        }
        return contactList
    }

    override fun getContactById(contactId: Int): Contact {
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
                val email1: String?
                val email2: String?
                val name =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val avatarUri: String? =
                    contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                val numbers = mutableListOf<String>()
                if (contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    numbers.addAll(
                        getPhoneNumbers(contactId)
                    )
                    number1 = numbers.firstOrNull()
                    number2 = numbers.getOrNull(1)
                }
                val emails = mutableListOf<String>()
                emails.addAll(getEmails(contactId))
                email1 = emails.firstOrNull()
                email2 = emails.getOrNull(1)
                val birthday = getBirthday(contactId)
                contact =
                    Contact(
                        contactId,
                        name,
                        number1,
                        number2,
                        email1,
                        email2,
                        avatarUri,
                        birthday
                    )
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

    private fun getEmails(contactId: Int): List<String> {
        val emails = mutableListOf<String>()
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
            arrayOf(contactId.toString()),
            null
        ).use { emailCursor ->
            while (emailCursor != null && emailCursor.moveToNext()) {
                emails.add(emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)))
            }
        }
        return emails
    }

    private fun getBirthday(contactId: Int): String? {
        val columns = arrayOf(
            ContactsContract.CommonDataKinds.Event.START_DATE,
            ContactsContract.CommonDataKinds.Event.TYPE,
            ContactsContract.CommonDataKinds.Event.MIMETYPE,
        )
        val where =
            ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                    " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and " + ContactsContract.Data.CONTACT_ID + " = " + contactId
        var birthday: String? = null
        context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            columns,
            where,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        ).use { birthdayCursor ->
            if (birthdayCursor != null) {
                while (birthdayCursor.moveToNext()) {
                    birthday =
                        birthdayCursor.getString(birthdayCursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
                }
            }
        }
        return birthday
    }
}
