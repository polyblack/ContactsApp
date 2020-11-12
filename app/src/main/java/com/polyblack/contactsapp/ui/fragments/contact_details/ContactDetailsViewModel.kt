package com.polyblack.contactsapp.ui.fragments.contact_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.repository.ContactsRepository
import kotlinx.coroutines.launch

class ContactDetailsViewModel(application: Application) :
    AndroidViewModel(application) {
    private val contactsRepository = ContactsRepository(application)
    private val _contact = MutableLiveData<ContactState>()
    val contact: LiveData<ContactState> = _contact

    fun getContact(contactId: Int) {
        viewModelScope.launch {
            _contact.value =
                ContactState(contactsRepository.getContactById(contactId))
        }
    }

    fun changeContactNotificationStatus(contactItem: ContactListItem.Item) {
        contactsRepository.changeContactNotificationStatus(contactItem)
    }
}
