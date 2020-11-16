package com.polyblack.contactsapp.di.app

import android.content.Context
import com.polyblack.contactsapp.data.datasource.ContactDataSource
import com.polyblack.contactsapp.di.viewmodel.ViewModelModule
import com.polyblack.contactsapp.ui.fragments.contact_details.ContactNotificationManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideDataSource(
        context: Context,
        notificationManager: ContactNotificationManager
    ): ContactDataSource = ContactDataSource(context, notificationManager)

    @Provides
    @Singleton
    fun provideNotificationManager(context: Context): ContactNotificationManager =
        ContactNotificationManager(context)
}
