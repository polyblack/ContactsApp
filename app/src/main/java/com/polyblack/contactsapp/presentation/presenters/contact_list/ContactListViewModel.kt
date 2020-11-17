package com.polyblack.contactsapp.presentation.presenters.contact_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.polyblack.contactsapp.presentation.presenters.ContactBaseViewModel
import com.polyblack.domain.entities.ContactListState
import com.polyblack.domain.interactors.contact_list.ContactListInteractor
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
    val contactListInteractor: ContactListInteractor
) : ContactBaseViewModel() {
    private val _contactList = MutableLiveData<ContactListState>()
    val contactList: LiveData<ContactListState> = _contactList

    fun getContacts() {
        compositeDisposable.add(
            contactListInteractor.getContactList()
                .subscribe({ data ->
                    _contactList.value = data
                }, { error ->
                    Log.e("listViewModel", error.toString())
                })
        )
    }

    fun showContactListByTyping(typing: String?): Boolean {
        _contactList.value = contactListInteractor.showContactListByTyping(typing)
        return false
    }
}
