package com.polyblack.contactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setContactListFragment()
        }
    }

    private fun setContactListFragment() {
        val contactListFragment = ContactListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, contactListFragment)
            .commit()
    }

    private fun setContactDetailsFragment() {
        val detailsFragment = ContactDetailsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailsFragment)
            .addToBackStack(null)
            .commit()
    }
}