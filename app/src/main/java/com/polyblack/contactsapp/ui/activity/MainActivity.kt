package com.polyblack.contactsapp.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.databinding.ActivityMainBinding
import com.polyblack.contactsapp.service.ContactsService
import com.polyblack.contactsapp.ui.ServiceIBinderDepend
import com.polyblack.contactsapp.ui.fragments.contact_details.ContactDetailsFragment
import com.polyblack.contactsapp.ui.fragments.contact_list.ContactListFragment

class MainActivity :
    AppCompatActivity(),
    ContactListFragment.OnContactSelectedListener,
    ToolbarBackButtonListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactsService: ContactsService
    private var iBinder: IBinder? = null
    private var bound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iBinder = service
            val binder = service as ContactsService.ContactsBinder
            contactsService = binder.getService()
            bound = true
            for (fragment in supportFragmentManager.fragments) {
                if (fragment is ServiceIBinderDepend) fragment.setServiceBinder(service)
            }
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

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        iBinder?.let { if (fragment is ServiceIBinderDepend) fragment.setServiceBinder(it) }
    }

    override fun onContactSelected(contactId: Int) {
        replaceWithContactDetailsFragment(contactId)
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

    private fun replaceWithContactDetailsFragment(contactId: Int) {
        val detailsFragment = ContactDetailsFragment.newInstance(contactId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailsFragment)
            .addToBackStack(null)
            .commit()
    }
}
