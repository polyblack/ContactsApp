package com.polyblack.domain.interactors.contact_details

import com.polyblack.domain.entities.Contact
import com.polyblack.domain.entities.ContactState
import com.polyblack.domain.repositories.contact_details.ContactDetailsRepository
import io.reactivex.Observable
import javax.inject.Inject

class ContactDetailsInteractorImpl @Inject constructor(val repository: ContactDetailsRepository) :
    ContactDetailsInteractor {
    override fun getContactById(contactId: Int): Observable<ContactState> =
        repository.getContactById(contactId).toObservable()
            .map { ContactState(false, null, it) }
            .startWith(ContactState(true, null, null))
            .onErrorReturn { error -> ContactState(false, error, null) }

    override fun getContactWithNewNotificationStatus(contact: Contact) =
        ContactState(false, null, repository.getContactWithNewNotificationStatus(contact))
}
