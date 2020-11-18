package com.polyblack.contactsapp.di.viewmodel

import androidx.lifecycle.ViewModel
import com.polyblack.contactsapp.presentation.presenters.contact_list.ContactListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ContactListViewModel::class)
    abstract fun bindContactListViewModel(contactListViewModel: ContactListViewModel): ViewModel
}
