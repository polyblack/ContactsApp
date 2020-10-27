package com.polyblack.contactsapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.polyblack.contactsapp.ui.OnContactsListResultListener

class ContactsService : Service() {
    private val binder = ContactsBinder()
    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    fun getContactList(listListener: OnContactsListResultListener) {
        //TODO асинхронность + отправка результата в фрагмент через listener
    }

    inner class ContactsBinder: Binder() {
        fun getService(): ContactsService = this@ContactsService
    }
}
