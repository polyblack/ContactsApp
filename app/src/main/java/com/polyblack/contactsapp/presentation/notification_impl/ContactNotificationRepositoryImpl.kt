package com.polyblack.contactsapp.presentation.notification_impl

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.presentation.broadcast_receivers.AlarmReceiver
import com.polyblack.domain.entities.Contact
import com.polyblack.domain.repositories.notification.ContactNotificationRepository
import javax.inject.Inject

class ContactNotificationRepositoryImpl @Inject constructor(
    val context: Context,
    val alarmManager: AlarmManager
) :
    ContactNotificationRepository {
    private val ACTION_NOTIFICATION = "CONTACT_BIRTHDAY_NOTIFICATION"
    private val EXTRA_NOTIFICATION = "NOTIFICATION_MESSAGE"
    private val EXTRA_CONTACT_ID = "CONTACT_ID"

    override fun getNotificationStatus(contact: Contact): Boolean =
        PendingIntent.getBroadcast(
            context,
            contact.id,
            createNotificationIntent(contact),
            PendingIntent.FLAG_NO_CREATE
        ) != null

    override fun setNotification(contact: Contact, millisToNotify: Long): Contact {
        createNotificationChannel()
        val alarmPendingIntent = createNotificationIntent(contact).let {
            PendingIntent.getBroadcast(
                context,
                contact.id,
                it,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            millisToNotify,
            alarmPendingIntent
        )
        contact.isNotificationOn = true
        return contact
    }

    override fun cancelNotification(contact: Contact): Contact {
        val alarmPendingIntent = createNotificationIntent(contact).let {
            PendingIntent.getBroadcast(
                context,
                contact.id,
                it,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
        alarmManager.cancel(alarmPendingIntent)
        alarmPendingIntent.cancel()
        contact.isNotificationOn = false
        return contact
    }

    private fun createNotificationIntent(contact: Contact): Intent =
        Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_NOTIFICATION
            putExtra(EXTRA_CONTACT_ID, contact.id)
            putExtra(
                EXTRA_NOTIFICATION,
                context.getString(R.string.notification_message) + " ${contact.name}"
            )
        }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                R.integer.channel_id.toString(),
                R.string.channel_name.toString(),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

