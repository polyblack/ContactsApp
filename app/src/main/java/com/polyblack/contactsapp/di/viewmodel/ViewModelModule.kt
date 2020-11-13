package com.polyblack.contactsapp.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polyblack.contactsapp.ui.fragments.ContactViewModelFactory
import com.polyblack.contactsapp.ui.fragments.contact_details.ContactDetailsViewModel
import com.polyblack.contactsapp.ui.fragments.contact_list.ContactListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ContactViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ContactListViewModel::class)
    abstract fun bindUserViewModel(contactListViewModel: ContactListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactDetailsViewModel::class)
    abstract fun bindSearchViewModel(contactDetailsViewModel: ContactDetailsViewModel): ViewModel
}
