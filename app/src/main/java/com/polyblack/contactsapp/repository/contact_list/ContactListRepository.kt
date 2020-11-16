package com.polyblack.contactsapp.repository.contact_list

import com.polyblack.contactsapp.data.datasource.ContactDataSource
import com.polyblack.contactsapp.data.model.ContactListItem
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ContactListRepository @Inject constructor(val dataSource: ContactDataSource) {
    fun getContactList(): Single<List<ContactListItem.Item>> =
        Single.fromCallable { dataSource.getContactList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
