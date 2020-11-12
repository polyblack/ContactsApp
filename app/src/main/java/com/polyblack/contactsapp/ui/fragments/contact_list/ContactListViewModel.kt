package com.polyblack.contactsapp.ui.fragments.contact_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.repository.ContactsRepository
import com.polyblack.contactsapp.ui.fragments.ContactBaseViewModel

class ContactListViewModel(application: Application) : ContactBaseViewModel(application) {
    private val contactsRepository = ContactsRepository(application)
    private val originalContactList = MutableLiveData<ContactListState>()
    private val _contactList = MutableLiveData<ContactListState>()
    val contactList: LiveData<ContactListState> = _contactList

    fun getContacts() {
        compositeDisposable.add(
            contactsRepository.getContactList()
                .doOnSubscribe {
                    _contactList.value =
                        ContactListState(true, null, listOf(ContactListItem.Loading))
                }
                .subscribe(
                    { data ->
                        _contactList.value = ContactListState(false, null, data)
                        originalContactList.value = _contactList.value
                    },
                    { error ->
                        _contactList.value = ContactListState(
                            false,
                            error,
                            listOf(ContactListItem.Error(error))
                        )
                    })
        )
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
