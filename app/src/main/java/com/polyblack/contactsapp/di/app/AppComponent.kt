package com.polyblack.contactsapp.di.app

import com.polyblack.contactsapp.ContactsApplication
import com.polyblack.contactsapp.di.contact_details.ContactDetailsComponent
import com.polyblack.contactsapp.di.contact_details.ContactDetailsModule
import com.polyblack.contactsapp.di.contact_list.ContactListComponent
import com.polyblack.contactsapp.di.contact_list.ContactListModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun plusContactListComponent(contactListModule: ContactListModule): ContactListComponent
    fun plusContactDetailsComponent(contactDetailsModule: ContactDetailsModule): ContactDetailsComponent
    fun inject(application: ContactsApplication)
}
