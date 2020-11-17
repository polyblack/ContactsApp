package com.polyblack.contactsapp.presentation.broadcast_receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.presentation.ui.activity.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    private val ACTION_NOTIFICATION = "CONTACT_BIRTHDAY_NOTIFICATION"
    private val ACTION_OPEN_DETAILS = "OPEN_DETAILS"
    private val EXTRA_NOTIFICATION = "NOTIFICATION_MESSAGE"
    private val EXTRA_CONTACT_ID = "CONTACT_ID"

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == ACTION_NOTIFICATION) {
            val activityIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                action = ACTION_OPEN_DETAILS
                putExtra(
                    EXTRA_CONTACT_ID,
                    intent.extras?.getInt(EXTRA_CONTACT_ID)
                )
            }
            val activityPendingIntent: PendingIntent = PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                0
            )
            val builder = NotificationCompat.Builder(context, R.integer.channel_id.toString())
                .setSmallIcon(R.drawable.ic_baseline_cake)
                .setContentTitle(context.resources.getString(R.string.birthday))
                .setContentText(intent.extras?.getString(EXTRA_NOTIFICATION))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityPendingIntent)
                .setAutoCancel(true)

            NotificationManagerCompat.from(context).run {
                notify(0, builder.build())
            }
        }
    }
}
