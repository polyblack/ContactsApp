package com.polyblack.data.repositories_impl.contact_list

import com.polyblack.data.datasource.ContactDataSource
import com.polyblack.domain.entities.Contact
import com.polyblack.domain.repositories.contact_list.ContactListRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ContactListRepositoryImpl @Inject constructor(val dataSource: ContactDataSource) :
    ContactListRepository {
    override fun getContactList(): Single<List<Contact>> =
        Single.fromCallable { dataSource.getContactList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
