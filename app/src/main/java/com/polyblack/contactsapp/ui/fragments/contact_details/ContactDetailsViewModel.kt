package com.polyblack.contactsapp.ui.fragments.contact_details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.polyblack.contactsapp.data.model.Contact
import com.polyblack.contactsapp.repository.ContactsRepository
import kotlinx.coroutines.launch

class ContactDetailsViewModel(application: Application) :
    AndroidViewModel(application) {
    private val contactsRepository = ContactsRepository(application)
    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact> = _contact

    fun getContact(contactId: Int) {
        viewModelScope.launch {
            val loadedContact = contactsRepository.getContactById(contactId)
            _contact.value = loadedContact
        }
    }
}
