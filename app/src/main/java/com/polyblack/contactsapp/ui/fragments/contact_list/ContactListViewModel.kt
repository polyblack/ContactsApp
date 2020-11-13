package com.polyblack.contactsapp.ui.fragments.contact_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.repository.contact_list.ContactListRepository
import com.polyblack.contactsapp.ui.fragments.ContactBaseViewModel
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
    val contactListRepository: ContactListRepository
) : ContactBaseViewModel() {
    private val originalContactList = MutableLiveData<ContactListState>()
    private val _contactList = MutableLiveData<ContactListState>()
    val contactList: LiveData<ContactListState> = _contactList

    fun getContacts() {
        compositeDisposable.add(
            contactListRepository.getContactList()
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
