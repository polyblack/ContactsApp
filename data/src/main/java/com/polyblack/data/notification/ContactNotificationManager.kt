package com.polyblack.data.notification

import android.app.PendingIntent
import com.polyblack.domain.entities.Contact

interface ContactNotificationManager {
    fun getNewNotificationStatus(contact: Contact): Boolean
    fun getNotificationStatus(contact: Contact): Boolean
    fun setNotification(birthday: String, pendingIntent : PendingIntent)
}
