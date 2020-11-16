package com.polyblack.contactsapp.di.contact_details

import com.polyblack.contactsapp.ui.fragments.contact_details.ContactDetailsFragment
import dagger.Subcomponent

@ContactDetailsScope
@Subcomponent(modules = [ContactDetailsModule::class])
interface ContactDetailsComponent {
    fun inject(contactDetailsFragment: ContactDetailsFragment)
}
