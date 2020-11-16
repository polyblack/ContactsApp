package com.polyblack.contactsapp.repository.contact_details

import com.polyblack.contactsapp.data.datasource.ContactDataSource
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.ui.fragments.contact_details.ContactNotificationManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ContactDetailsRepository @Inject constructor(val dataSource: ContactDataSource, val contactNotificationManager: ContactNotificationManager) {
    fun getContactById(contactId: Int): Single<ContactListItem.Item> =
        Single.fromCallable { dataSource.getContactById(contactId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun changeContactNotificationStatus(contactItem: ContactListItem.Item) {
        contactNotificationManager.changeNotificationState(contactItem)
    }
}
