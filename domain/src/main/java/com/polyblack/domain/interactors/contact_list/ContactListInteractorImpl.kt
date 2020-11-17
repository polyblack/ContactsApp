package com.polyblack.domain.interactors.contact_list

import com.polyblack.domain.entities.ContactListItem
import com.polyblack.domain.entities.ContactListState
import com.polyblack.domain.repositories.contact_list.ContactListRepository
import io.reactivex.Observable
import javax.inject.Inject

class ContactListInteractorImpl @Inject constructor(val repository: ContactListRepository) :
    ContactListInteractor {
    private var contactList: List<ContactListItem.Item>? = null
    override fun getContactList(): Observable<ContactListState> =
        repository.getContactList().toObservable()
            .map {
                val tempList = mutableListOf<ContactListItem.Item>()
                for (item in it) {
                    tempList.add(ContactListItem.Item(item))
                }
                contactList = tempList
                ContactListState(tempList)
            }
            .startWith(ContactListState(listOf(ContactListItem.Loading)))
            .onErrorReturn { error -> ContactListState(listOf(ContactListItem.Error(error))) }

    override fun showContactListByTyping(typing: String?): ContactListState? {
        typing?.let { typingStr ->
            contactList?.filter {
                it.contact.name.contains(
                    typingStr,
                    true
                )
            }?.let { filtered ->
                return ContactListState(filtered)
            }
        } ?: return contactList?.let { ContactListState(it) }
    }
}
