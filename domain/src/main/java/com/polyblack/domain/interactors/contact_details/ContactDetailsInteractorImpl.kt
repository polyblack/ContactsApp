package com.polyblack.domain.interactors.contact_details

import com.polyblack.domain.entities.ContactState
import com.polyblack.domain.repositories.contact_details.ContactDetailsRepository
import com.polyblack.domain.repositories.notification.ContactNotificationRepository
import io.reactivex.Observable
import javax.inject.Inject

class ContactDetailsInteractorImpl @Inject constructor(
    val contactDetailsRepository: ContactDetailsRepository,
    val notificationRepository: ContactNotificationRepository
) :
    ContactDetailsInteractor {
    override fun getContactById(contactId: Int): Observable<ContactState> =
        contactDetailsRepository.getContactById(contactId).toObservable()
            .map { contact ->
                contact.birthday?.let {
                    contact.isNotificationOn = notificationRepository.getNotificationStatus(contact)
                }
                ContactState(false, null, contact)
            }
            .startWith(ContactState(true, null, null))
            .onErrorReturn { error -> ContactState(false, error, null) }
}
