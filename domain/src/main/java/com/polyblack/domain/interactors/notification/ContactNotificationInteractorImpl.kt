package com.polyblack.domain.interactors.notification

import com.polyblack.domain.entities.Contact
import com.polyblack.domain.entities.ContactState
import com.polyblack.domain.interactors.calendar.NotificationCalendar
import com.polyblack.domain.repositories.notification.ContactNotificationRepository
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

class ContactNotificationInteractorImpl @Inject constructor(
    val repository: ContactNotificationRepository,
    val notificationCalendar: NotificationCalendar
) : ContactNotificationInteractor {
    override fun getNewNotificationStatus(contact: Contact): ContactState {
        return if (!getNotificationStatus(contact)) {
            ContactState(
                false,
                null,
                contact.birthday?.let {
                    getMillisToNotify(it)
                }?.let {
                    repository.setNotification(
                        contact,
                        it
                    )
                }
            )
        } else {
            ContactState(false, null, repository.cancelNotification(contact))
        }
    }

    override fun getNotificationStatus(contact: Contact): Boolean =
        repository.getNotificationStatus(contact)

    private fun getMillisToNotify(date: String): Long {
        val leapPeriod = 4
        val february = 1
        val day29 = 29
        val sdf = SimpleDateFormat("--dd-MM", Locale("ru"))
        var birthdayYear = notificationCalendar.getCalendar().get(Calendar.YEAR)
        var calendar = GregorianCalendar.getInstance() as GregorianCalendar
        if (date == "--29-02") {
            if (!calendar.isLeapYear(birthdayYear)) {
                birthdayYear = (birthdayYear / leapPeriod) * leapPeriod + leapPeriod
                calendar = GregorianCalendar(birthdayYear, february, day29)
            } else {
                calendar = GregorianCalendar(birthdayYear, february, day29)
                if (calendar.timeInMillis < notificationCalendar.getCalendar().timeInMillis) {
                    birthdayYear++
                    birthdayYear = (birthdayYear / leapPeriod) * leapPeriod + leapPeriod
                    calendar.set(Calendar.YEAR, birthdayYear)
                }
            }
        } else {
            calendar.run {
                time = sdf.parse(date)
                set(Calendar.YEAR, birthdayYear)
            }
            if (calendar.timeInMillis < notificationCalendar.getCalendar().timeInMillis) {
                birthdayYear++
                calendar.set(Calendar.YEAR, birthdayYear)
            }
        }

        Logger.getLogger("fragment_notifInter").log(
            Level.INFO,
            "y: ${calendar.get(Calendar.YEAR)} m: ${calendar.get(Calendar.MONTH)} d: ${
                calendar.get(
                    Calendar.DAY_OF_MONTH
                )
            } millis: ${calendar.timeInMillis}"
        )
        return calendar.timeInMillis
    }
}
