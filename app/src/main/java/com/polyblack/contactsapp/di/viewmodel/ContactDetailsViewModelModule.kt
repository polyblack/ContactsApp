package com.polyblack.contactsapp.di.viewmodel

import androidx.lifecycle.ViewModel
import com.polyblack.contactsapp.presentation.presenters.contact_details.ContactDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactDetailsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    abstract fun bindContactDetailsViewModel(contactDetailsViewModel: ContactDetailsViewModel): ViewModel
}
