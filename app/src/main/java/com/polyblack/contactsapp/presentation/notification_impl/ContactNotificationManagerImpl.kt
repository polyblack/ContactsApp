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
import com.polyblack.contactsapp.utils.DateUtils
import com.polyblack.data.notification.ContactNotificationManager
import com.polyblack.domain.entities.Contact
import javax.inject.Inject

class ContactNotificationManagerImpl @Inject constructor(val context: Context) :
    ContactNotificationManager {
    private val ACTION_NOTIFICATION = "CONTACT_BIRTHDAY_NOTIFICATION"
    private val EXTRA_NOTIFICATION = "NOTIFICATION_MESSAGE"
    private val EXTRA_CONTACT_ID = "CONTACT_ID"

    override fun getNewNotificationStatus(contact: Contact): Boolean {
        val alarmPendingIntent = createNotificationIntent(contact).let {
            PendingIntent.getBroadcast(
                context,
                contact.id,
                it,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        when (contact.isNotificationOn) {
            false -> {
                contact.birthday?.let {
                    createNotificationChannel()
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + DateUtils.getTimeLeftInMillis(it),
                        alarmPendingIntent
                    )
                    return true
                } ?: return false
            }
            true -> {
                if (getNotificationStatus(contact)) {
                    alarmManager.cancel(alarmPendingIntent)
                    alarmPendingIntent.cancel()
                }
                return false
            }
            else -> {
                return false
            }
        }
    }

    override fun getNotificationStatus(contact: Contact): Boolean =
        PendingIntent.getBroadcast(
            context,
            contact.id,
            createNotificationIntent(contact),
            PendingIntent.FLAG_NO_CREATE
        ) != null

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

