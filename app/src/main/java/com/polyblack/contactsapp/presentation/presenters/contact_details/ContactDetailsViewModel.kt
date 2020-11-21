package com.polyblack.contactsapp.presentation.presenters.contact_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polyblack.contactsapp.presentation.presenters.ContactBaseViewModel
import com.polyblack.domain.entities.Contact
import com.polyblack.domain.entities.ContactState
import com.polyblack.domain.interactors.contact_details.ContactDetailsInteractor
import com.polyblack.domain.interactors.notification.ContactNotificationInteractor
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(
    val contactDetailsInteractor: ContactDetailsInteractor,
    val contactNotificationInteractor: ContactNotificationInteractor
) :
    ContactBaseViewModel() {
    private val _contact = MutableLiveData<ContactState>()
    val contact: LiveData<ContactState> = _contact

    fun getContact(contactId: Int) {
        compositeDisposable.add(
            contactDetailsInteractor.getContactById(contactId)
                .subscribe({ data ->
                    _contact.value = data
                }, { error ->
                    Log.e("detailsViewModel", error.toString())
                })
        )
    }

    fun getContactWithNewNotificationStatus(contact: Contact) {
        _contact.value = contactNotificationInteractor.getNewNotificationStatus(contact)
    }
}
