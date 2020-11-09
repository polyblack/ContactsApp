package com.polyblack.contactsapp

import android.app.Application
import android.content.Context

class ContactsApplication : Application() {
    companion object {
        lateinit var application: Application
        val context: Context
            get() = application.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}
