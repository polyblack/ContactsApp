package com.polyblack.data.repositories_impl.contact_details

import com.polyblack.data.datasource.ContactDataSource
import com.polyblack.domain.entities.Contact
import com.polyblack.domain.repositories.contact_details.ContactDetailsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ContactDetailsRepositoryImpl @Inject constructor(val dataSource: ContactDataSource) :
    ContactDetailsRepository {
    override fun getContactById(contactId: Int): Single<Contact> =
        Single.fromCallable { dataSource.getContactById(contactId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getContactWithNewNotificationStatus(contact: Contact) =
        dataSource.getContactWithNewNotificationStatus(contact)
}
