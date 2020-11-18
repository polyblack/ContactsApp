package com.polyblack.contactsapp.di.contact_list

import com.polyblack.contactsapp.di.viewmodel.ContactListViewModelModule
import com.polyblack.data.datasource.ContactDataSource
import com.polyblack.data.repositories_impl.contact_list.ContactListRepositoryImpl
import com.polyblack.domain.interactors.contact_list.ContactListInteractor
import com.polyblack.domain.interactors.contact_list.ContactListInteractorImpl
import com.polyblack.domain.repositories.contact_list.ContactListRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ContactListViewModelModule::class])
class ContactListModule {
    @ContactListScope
    @Provides
    fun provideListRepository(dataSource: ContactDataSource): ContactListRepository =
        ContactListRepositoryImpl(dataSource)

    @ContactListScope
    @Provides
    fun provideListInteractor(repository: ContactListRepository): ContactListInteractor =
        ContactListInteractorImpl(repository)
}
