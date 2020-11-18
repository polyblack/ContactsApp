package com.polyblack.domain.interactors.contact_details

import com.polyblack.domain.entities.Contact
import com.polyblack.domain.entities.ContactState
import io.reactivex.Observable

interface ContactDetailsInteractor {
    fun getContactById(contactId: Int): Observable<ContactState>
    fun getContactWithNewNotificationStatus(contact: Contact): ContactState
}
