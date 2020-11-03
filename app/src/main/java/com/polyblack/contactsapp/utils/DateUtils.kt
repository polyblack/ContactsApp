package com.polyblack.contactsapp.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun getTimeLeftInMillis(date: String?): Long {
            val sdf = SimpleDateFormat("dd-MM", Locale("ru"))
            val calendar: GregorianCalendar = GregorianCalendar.getInstance() as GregorianCalendar
            calendar.run {
                time = sdf.parse(date)
                set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) + 1)
                set(Calendar.HOUR_OF_DAY, 10)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            return calendar.timeInMillis - System.currentTimeMillis()
        }
    }
}
