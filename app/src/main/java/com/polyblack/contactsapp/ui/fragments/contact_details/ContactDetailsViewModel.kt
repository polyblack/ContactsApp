package com.polyblack.contactsapp.ui.fragments.contact_details

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.repository.ContactsRepository
import com.polyblack.contactsapp.ui.fragments.ContactBaseViewModel

class ContactDetailsViewModel(application: Application) : ContactBaseViewModel(application) {
    private val contactsRepository = ContactsRepository(application)
    private val _contact = MutableLiveData<ContactState>()
    val contact: LiveData<ContactState> = _contact

    fun getContact(contactId: Int) {
        compositeDisposable.add(
            contactsRepository.getContactById(contactId)
                .doOnSubscribe {
                    _contact.value =
                        ContactState(true, null, ContactListItem.Loading)
                }
                .subscribe(
                    { data ->
                        _contact.value = ContactState(false, null, data)
                    },
                    { error ->
                        _contact.value = ContactState(
                            false,
                            error,
                            ContactListItem.Error(error)
                        )
                    })
        )
    }

    fun changeContactNotificationStatus(contactItem: ContactListItem.Item) {
        contactsRepository.changeContactNotificationStatus(contactItem)
    }
}
