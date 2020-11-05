package com.polyblack.contactsapp.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.polyblack.contactsapp.repository.ContactsRepository
import kotlinx.coroutines.*

class ContactsService : Service() {
    private val binder = ContactsBinder()
    private val job = SupervisorJob()
    private lateinit var scope: CoroutineScope
    val ACTION_CONTACT = "GET_CONTACT"
    val ACTION_CONTACT_LIST = "GET_CONTACT_LIST"
    val NAME_CONTACT = "CONTACT"
    val NAME_CONTACT_LIST = "CONTACT_LIST"

    override fun onBind(p0: Intent?): IBinder? = binder

    override fun onCreate() {
        super.onCreate()
        scope = CoroutineScope(Dispatchers.IO + job)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    fun getContactList(context: Context) {
        scope.launch {
            val contactList = withContext(Dispatchers.IO) {
                ContactsRepository(context).getContactList()
            }
            val intent = Intent()
            intent.action = ACTION_CONTACT_LIST
            intent.putParcelableArrayListExtra(NAME_CONTACT_LIST, contactList)
            sendBroadcast(intent)
        }
    }

    fun getContactById(context: Context, contactId: Int) {
        scope.launch {
            val contact = withContext(Dispatchers.IO) {
                ContactsRepository(context).getContactById(contactId)
            }
            val intent = Intent()
            intent.action = ACTION_CONTACT
            intent.putExtra(NAME_CONTACT, contact)
            sendBroadcast(intent)
        }
    }

    inner class ContactsBinder : Binder() {
        fun getService(): ContactsService = this@ContactsService
    }
}
