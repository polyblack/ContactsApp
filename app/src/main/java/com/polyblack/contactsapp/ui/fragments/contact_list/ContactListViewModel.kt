package com.polyblack.contactsapp.ui.fragments.contact_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.repository.ContactsRepository
import kotlinx.coroutines.launch
import java.io.IOException

class ContactListViewModel(application: Application) : AndroidViewModel(application) {
    private val contactsRepository = ContactsRepository(application)
    private val originalContactList = MutableLiveData<ContactListState>()
    private val _contactList = MutableLiveData<ContactListState>()
    val contactList: LiveData<ContactListState> = _contactList
    fun getContacts() {
        viewModelScope.launch {
            _contactList.value =
                ContactListState(true, null, listOf(ContactListItem.Loading))
            try {
                originalContactList.value =
                    ContactListState(false, null, contactsRepository.getContactList())
                _contactList.value = originalContactList.value
            } catch (exception: IOException) {
                _contactList.value =
                    ContactListState(false, exception, listOf(ContactListItem.Error(exception)))
            }
        }
    }

    fun showContactListByTyping(typing: String?): Boolean {
        typing?.let { typingStr ->
            originalContactList.value?.data?.filter {
                it is ContactListItem.Item &&
                        it.contact.name.contains(
                            typingStr,
                            true
                        )
            }?.let { filtered ->
                _contactList.value = ContactListState(false, null, filtered)
            }
        }
        return false
    }
}
