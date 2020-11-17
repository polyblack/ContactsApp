package com.polyblack.contactsapp.di.contact_details

import com.polyblack.contactsapp.di.viewmodel.ContactDetailsViewModelModule
import com.polyblack.data.datasource.ContactDataSource
import com.polyblack.data.repositories_impl.contact_details.ContactDetailsRepositoryImpl
import com.polyblack.domain.interactors.contact_details.ContactDetailsInteractor
import com.polyblack.domain.interactors.contact_details.ContactDetailsInteractorImpl
import com.polyblack.domain.repositories.contact_details.ContactDetailsRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ContactDetailsViewModelModule::class])
class ContactDetailsModule {
    @Provides
    @ContactDetailsScope
    fun provideDetailsRepository(
        dataSource: ContactDataSource
    ): ContactDetailsRepository = ContactDetailsRepositoryImpl(dataSource)

    @Provides
    @ContactDetailsScope
    fun provideDetailsInteractor(repository: ContactDetailsRepository): ContactDetailsInteractor =
        ContactDetailsInteractorImpl(repository)
}
