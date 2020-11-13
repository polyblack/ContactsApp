package com.polyblack.contactsapp.ui.fragments.contact_details

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.utils.DateUtils
import javax.inject.Inject

class ContactNotificationManager @Inject constructor(val context: Context) {
    private val ACTION_NOTIFICATION = "CONTACT_BIRTHDAY_NOTIFICATION"
    private val EXTRA_NOTIFICATION = "NOTIFICATION_MESSAGE"
    private val EXTRA_CONTACT_ID = "CONTACT_ID"
    fun changeNotificationState(contactItem: ContactListItem.Item) {
        val alarmPendingIntent = createNotificationIntent(contactItem).let {
            PendingIntent.getBroadcast(
                context,
                contactItem.contact.id,
                it,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        when (contactItem.contact.isNotificationOn) {
            false -> {
                contactItem.contact.birthday?.let {
                    createNotificationChannel()
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + DateUtils.getTimeLeftInMillis(contactItem.contact.birthday),
                        alarmPendingIntent
                    )
                } ?: run {
                    Toast.makeText(
                        context,
                        context.getString(R.string.no_birthday),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            true -> {
                if (checkIfNotificationIsEnabled(contactItem)) {
                    alarmManager.cancel(alarmPendingIntent)
                    alarmPendingIntent.cancel()
                }
            }
        }
    }

    fun checkIfNotificationIsEnabled(contactItem: ContactListItem.Item): Boolean =
        PendingIntent.getBroadcast(
            context,
            contactItem.contact.id,
            createNotificationIntent(contactItem),
            PendingIntent.FLAG_NO_CREATE
        ) != null

    private fun createNotificationIntent(contactItem: ContactListItem.Item): Intent =
        Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_NOTIFICATION
            putExtra(EXTRA_CONTACT_ID, contactItem.contact.id)
            putExtra(
                EXTRA_NOTIFICATION,
                context.getString(R.string.notification_message) + " ${contactItem.contact.name}"
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

