package com.polyblack.data.notification

import com.polyblack.domain.entities.Contact

interface ContactNotificationManager {
    fun getNewNotificationStatus(contact: Contact): Boolean
    fun getNotificationStatus(contact: Contact): Boolean
}
