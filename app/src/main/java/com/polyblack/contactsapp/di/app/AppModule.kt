package com.polyblack.contactsapp.di.app

import android.content.Context
import com.polyblack.contactsapp.di.viewmodel.ViewModelFactoryModule
import com.polyblack.data.datasource.ContactDataSource
import com.polyblack.data.datasource.ContactDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelFactoryModule::class])
class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context = context

    @Provides
    @Singleton
    fun provideDataSource(
        context: Context
    ): ContactDataSource = ContactDataSourceImpl(context)
}
