package com.polyblack.contactsapp.di.contact_list

import com.polyblack.contactsapp.presentation.ui.fragments.contact_list.ContactListFragment
import dagger.Subcomponent

@ContactListScope
@Subcomponent(modules = [ContactListModule::class])
interface ContactListComponent {
    fun inject(contactListFragment: ContactListFragment)
}
