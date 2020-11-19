package com.polyblack.domain.interactors.calendar

import java.util.*

class NotificationCalendarImpl: NotificationCalendar {
    override fun getCalendar(): GregorianCalendar = GregorianCalendar.getInstance() as GregorianCalendar
}
