package com.polyblack.contactsapp.ui.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
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
    ContactListFragment.OnContactSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactsService: ContactsService
    private val ACTION_OPEN_DETAILS = "OPEN_DETAILS"
    private val EXTRA_CONTACT_ID = "CONTACT_ID"
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
        val intentService = Intent(this, ContactsService::class.java)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        iBinder?.let { if (fragment is ServiceIBinderDepend) fragment.setServiceBinder(it) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (intent?.action == ACTION_OPEN_DETAILS) {
            openAfterNotificationClicked()
        }
    }

    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
            bound = false
        }
        super.onDestroy()
    }

    override fun onContactSelected(contactId: Int) {
        replaceWithContactDetailsFragment(contactId)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                R.integer.channel_id.toString(),
                R.string.channel_name.toString(),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun openAfterNotificationClicked() {
        if (getCurrentFragment() is ContactDetailsFragment) {
            intent.extras?.getInt(EXTRA_CONTACT_ID)
                ?.let { replaceWithContactDetailsFragmentNoBackStack(it) }
        } else {
            intent.extras?.getInt(EXTRA_CONTACT_ID)?.let { replaceWithContactDetailsFragment(it) }
        }
        intent = null
    }

    private fun addContactListFragment() {
        val contactListFragment = ContactListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, contactListFragment)
            .commit()
    }

    private fun replaceWithContactDetailsFragmentNoBackStack(contactId: Int) {
        val detailsFragment = ContactDetailsFragment.newInstance(contactId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailsFragment)
            .commit()
    }

    private fun replaceWithContactDetailsFragment(contactId: Int) {
        val detailsFragment = ContactDetailsFragment.newInstance(contactId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getCurrentFragment(): Fragment? =
        supportFragmentManager.findFragmentById(R.id.fragment_container)
}
