package com.polyblack.contactsapp.di.contact_list

import com.polyblack.contactsapp.data.datasource.ContactDataSource
import com.polyblack.contactsapp.repository.contact_list.ContactListRepository
import dagger.Module
import dagger.Provides

@Module
class ContactListModule {

    @Provides
    @ContactListScope
    fun provideRepository(dataSource: ContactDataSource): ContactListRepository =
        ContactListRepository(dataSource)
}
