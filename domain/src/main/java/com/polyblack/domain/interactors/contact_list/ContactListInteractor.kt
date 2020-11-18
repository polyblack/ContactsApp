package com.polyblack.domain.interactors.contact_list

import com.polyblack.domain.entities.ContactListState
import io.reactivex.Observable

interface ContactListInteractor {
    fun getContactList(): Observable<ContactListState>
    fun showContactListByTyping(typing: String?): ContactListState?
}
