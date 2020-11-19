package com.polyblack.domain.repositories.contact_details

import com.polyblack.domain.entities.Contact
import io.reactivex.Single

interface ContactDetailsRepository {
    fun getContactById(contactId: Int): Single<Contact>
}
