package com.polyblack.contactsapp.utils

import com.polyblack.domain.interactors.calendar.NotificationCalendar
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun getTimeLeftInMillis(date: String, notificationCalendar: NotificationCalendar): Long {
            val sdf = SimpleDateFormat("--dd-MM", Locale("ru"))
            var birthdayYear = notificationCalendar.getCalendar().get(Calendar.YEAR)
            val calendar = GregorianCalendar.getInstance() as GregorianCalendar
            calendar.run {
                time = sdf.parse(date)
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            if (calendar.timeInMillis < notificationCalendar.getCalendar().timeInMillis) {
                birthdayYear++
            }
            if (calendar.get(Calendar.MONTH) == 1 && calendar.get(Calendar.DAY_OF_MONTH) == 29 && !calendar.isLeapYear(
                    birthdayYear
                )
            ) {
                birthdayYear = (birthdayYear / 4) * 4 + 4
            }
            calendar.set(Calendar.YEAR, birthdayYear)
            return calendar.timeInMillis
        }
    }
}
