package com.polyblack.contactsapp.di.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.polyblack.contactsapp.presentation.presenters.ContactViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ContactViewModelFactory): ViewModelProvider.Factory
}
