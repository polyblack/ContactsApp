package com.polyblack.contactsapp.ui.fragments.contact_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.repository.contact_details.ContactDetailsRepository
import com.polyblack.contactsapp.ui.fragments.ContactBaseViewModel
import javax.inject.Inject

class ContactDetailsViewModel @Inject constructor(val contactDetailsRepository: ContactDetailsRepository) :
    ContactBaseViewModel() {
    private val _contact = MutableLiveData<ContactState>()
    val contact: LiveData<ContactState> = _contact

    fun getContact(contactId: Int) {
        compositeDisposable.add(
            contactDetailsRepository.getContactById(contactId)
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
        contactDetailsRepository.changeContactNotificationStatus(contactItem)
    }
}
