package com.polyblack.contactsapp.ui.fragments.contact_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.polyblack.contactsapp.data.model.Contact
import com.polyblack.contactsapp.repository.ContactsRepository
import kotlinx.coroutines.launch

class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val contactsRepository = ContactsRepository(application)
    private val _contactList = MutableLiveData<List<Contact>>()
    val contactList: LiveData<List<Contact>> = _contactList

    fun getContacts() {
        viewModelScope.launch {
            val loadedList = contactsRepository.getContactList()
            _contactList.value = loadedList
        }
    }
}
