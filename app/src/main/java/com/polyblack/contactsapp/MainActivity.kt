package com.polyblack.contactsapp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.polyblack.contactsapp.databinding.ActivityMainBinding
import com.polyblack.contactsapp.ui.*

class MainActivity :
    AppCompatActivity(),
    ContactListFragment.OnContactSelectedListener,
    ToolbarBackButtonListener,
    ServiceCommandsListener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var contactsService: ContactsService
    private var bound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ContactsService.ContactsBinder
            contactsService = binder.getService()
            bound = true

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            addContactListFragment()
        }
        val intent = Intent(this, ContactsService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
            bound = false
        }
        super.onDestroy()
    }

    override fun onContactSelected(id: Int) {
        getContactById(id)
        replaceWithContactDetailsFragment(id)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun setButtonVisibility(isVisible: Boolean) {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(isVisible)
            it.setDisplayShowHomeEnabled(isVisible)
        }
    }

    private fun addContactListFragment() {
        val contactListFragment = ContactListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, contactListFragment)
            .commit()
    }

    private fun replaceWithContactDetailsFragment(id: Int) {
        val detailsFragment = ContactDetailsFragment.newInstance(id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun getContactList(listListener: OnContactsListResultListener){
        contactsService.getContactList(listListener)
    }

    override fun getContactById(contactId: Int) {

    }
}
