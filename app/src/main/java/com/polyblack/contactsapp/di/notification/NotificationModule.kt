package com.polyblack.contactsapp.di.notification

import android.app.AlarmManager
import android.content.Context
import com.polyblack.contactsapp.di.contact_details.ContactDetailsScope
import com.polyblack.contactsapp.presentation.notification_impl.ContactNotificationRepositoryImpl
import com.polyblack.domain.interactors.calendar.NotificationCalendar
import com.polyblack.domain.interactors.calendar.NotificationCalendarImpl
import com.polyblack.domain.interactors.notification.ContactNotificationInteractor
import com.polyblack.domain.interactors.notification.ContactNotificationInteractorImpl
import com.polyblack.domain.repositories.notification.ContactNotificationRepository
import dagger.Module
import dagger.Provides

@Module
class NotificationModule {
    @Provides
    @ContactDetailsScope
    fun provideNotificationCalendar(): NotificationCalendar = NotificationCalendarImpl()

    @Provides
    @ContactDetailsScope
    fun provideAlarmManager(context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    @ContactDetailsScope
    fun provideNotificationRepository(
        context: Context,
        alarmManager: AlarmManager
    ): ContactNotificationRepository =
        ContactNotificationRepositoryImpl(context, alarmManager)

    @Provides
    @ContactDetailsScope
    fun provideNotificationInteractor(
        contactNotificationRepository: ContactNotificationRepository,
        notificationCalendar: NotificationCalendar
    ): ContactNotificationInteractor =
        ContactNotificationInteractorImpl(contactNotificationRepository, notificationCalendar)
}
