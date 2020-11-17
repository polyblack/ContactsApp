package com.polyblack.contactsapp.di.app

import android.content.Context
import com.polyblack.contactsapp.di.viewmodel.ViewModelFactoryModule
import com.polyblack.contactsapp.presentation.notification_impl.ContactNotificationManagerImpl
import com.polyblack.data.datasource.ContactDataSource
import com.polyblack.data.datasource.ContactDataSourceImpl
import com.polyblack.data.notification.ContactNotificationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelFactoryModule::class])
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideNotificationManager(context: Context): ContactNotificationManager =
        ContactNotificationManagerImpl(context)

    @Provides
    @Singleton
    fun provideDataSource(
        context: Context,
        notificationManager: ContactNotificationManager
    ): ContactDataSource = ContactDataSourceImpl(context, notificationManager)
}
