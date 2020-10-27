package com.polyblack.contactsapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class ContactsService : Service() {
    private val binder = ContactsBinder()
    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class ContactsBinder: Binder() {
        fun getService(): ContactsService = this@ContactsService
    }
}