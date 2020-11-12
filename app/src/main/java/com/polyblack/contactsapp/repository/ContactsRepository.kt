package com.polyblack.contactsapp.repository

import android.content.Context
import com.polyblack.contactsapp.data.datasource.ContactDataSource
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.ui.fragments.contact_details.ContactNotificationManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ContactsRepository(context: Context) {
    private val dataSource = ContactDataSource(context)
    fun getContactList(): Single<List<ContactListItem.Item>> =
        Single.fromCallable { dataSource.getContactList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getContactById(contactId: Int): Single<ContactListItem.Item> =
        Single.fromCallable { dataSource.getContactById(contactId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun changeContactNotificationStatus(contactItem: ContactListItem.Item) {
        ContactNotificationManager.changeNotificationState(contactItem)
    }
}
