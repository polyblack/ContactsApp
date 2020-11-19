package com.polyblack.contactsapp.di.contact_details

import com.polyblack.contactsapp.di.notification.NotificationModule
import com.polyblack.contactsapp.di.viewmodel.ContactDetailsViewModelModule
import com.polyblack.data.datasource.ContactDataSource
import com.polyblack.data.repositories_impl.contact_details.ContactDetailsRepositoryImpl
import com.polyblack.domain.interactors.contact_details.ContactDetailsInteractor
import com.polyblack.domain.interactors.contact_details.ContactDetailsInteractorImpl
import com.polyblack.domain.repositories.contact_details.ContactDetailsRepository
import com.polyblack.domain.repositories.notification.ContactNotificationRepository
import dagger.Module
import dagger.Provides

@Module(includes = [ContactDetailsViewModelModule::class, NotificationModule::class])
class ContactDetailsModule {
    @ContactDetailsScope
    @Provides
    fun provideDetailsInteractor(
        contactDetailsRepository: ContactDetailsRepository,
        notificationRepository: ContactNotificationRepository,
    ): ContactDetailsInteractor =
        ContactDetailsInteractorImpl(contactDetailsRepository, notificationRepository)

    @Provides
    @ContactDetailsScope
    fun provideDetailsRepository(
        dataSource: ContactDataSource
    ): ContactDetailsRepository = ContactDetailsRepositoryImpl(dataSource)
}
