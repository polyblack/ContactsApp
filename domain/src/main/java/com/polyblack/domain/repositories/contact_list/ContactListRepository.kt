package com.polyblack.domain.repositories.contact_list

import com.polyblack.domain.entities.Contact
import io.reactivex.Single

interface ContactListRepository {
    fun getContactList(): Single<List<Contact>>
}
