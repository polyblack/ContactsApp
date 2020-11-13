package com.polyblack.contactsapp

import android.app.Application
import com.polyblack.contactsapp.di.app.AppComponent
import com.polyblack.contactsapp.di.app.AppModule
import com.polyblack.contactsapp.di.app.DaggerAppComponent

class ContactsApplication : Application() {
    private var appComponent: AppComponent? = null
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    fun getAppComponent() = appComponent
}
