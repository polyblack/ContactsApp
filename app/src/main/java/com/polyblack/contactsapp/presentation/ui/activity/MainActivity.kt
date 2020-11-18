package com.polyblack.contactsapp.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.databinding.ActivityMainBinding
import com.polyblack.contactsapp.presentation.ui.fragments.contact_details.ContactDetailsFragment
import com.polyblack.contactsapp.presentation.ui.fragments.contact_list.ContactListFragment

class MainActivity :
    AppCompatActivity(),
    ContactListFragment.OnContactSelectedListener,
    FragmentManager.OnBackStackChangedListener {
    private lateinit var binding: ActivityMainBinding
    private val ACTION_OPEN_DETAILS = "OPEN_DETAILS"
    private val EXTRA_CONTACT_ID = "CONTACT_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            addContactListFragment()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.addOnBackStackChangedListener(this)
        shouldDisplayHomeUp()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (intent?.action == ACTION_OPEN_DETAILS) {
            openAfterNotificationClicked()
        }
    }

    override fun onContactSelected(contactId: Int) {
        replaceWithContactDetailsFragment(contactId)
    }

    override fun onBackStackChanged() {
        shouldDisplayHomeUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun shouldDisplayHomeUp() {
        supportActionBar?.setDisplayHomeAsUpEnabled(supportFragmentManager.backStackEntryCount > 0)
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
