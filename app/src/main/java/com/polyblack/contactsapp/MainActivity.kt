package com.polyblack.contactsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.polyblack.contactsapp.ui.ContactDetailsFragment
import com.polyblack.contactsapp.ui.ContactListFragment
import com.polyblack.contactsapp.ui.ToolbarBackButtonListener

class MainActivity : AppCompatActivity(), ContactListFragment.OnContactSelectedListener,
    ToolbarBackButtonListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
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

    private fun setContactDetailsFragment(id: Int) {
        val detailsFragment = ContactDetailsFragment.newInstance(id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onContactSelected(id: Int) {
        setContactDetailsFragment(id)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun setButtonVisibility(isVisible: Boolean) {
        supportActionBar.let {
            it?.setDisplayHomeAsUpEnabled(isVisible)
            it?.setDisplayShowHomeEnabled(isVisible)
        }
    }
}