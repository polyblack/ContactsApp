package com.polyblack.contactsapp.di.contact_details

import com.polyblack.contactsapp.data.datasource.ContactDataSource
import com.polyblack.contactsapp.repository.contact_details.ContactDetailsRepository
import com.polyblack.contactsapp.ui.fragments.contact_details.ContactNotificationManager
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule {

    @Provides
    @ContactDetailsScope
    fun provideRepository(
        dataSource: ContactDataSource,
        contactNotificationManager: ContactNotificationManager
    ): ContactDetailsRepository = ContactDetailsRepository(dataSource, contactNotificationManager)
}
