package com.polyblack.domain.interactors.notification

import com.polyblack.domain.entities.Contact
import com.polyblack.domain.entities.ContactState

interface ContactNotificationInteractor {
    fun getNewNotificationStatus(contact: Contact): ContactState
    fun getNotificationStatus(contact: Contact): Boolean
}
