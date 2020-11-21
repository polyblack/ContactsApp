package com.polyblack.domain.repositories.notification

import com.polyblack.domain.entities.Contact

interface ContactNotificationRepository {
    fun getNotificationStatus(contact: Contact): Boolean
    fun setNotification(contact: Contact, millisToNotify: Long): Contact
    fun cancelNotification(contact: Contact): Contact
}
