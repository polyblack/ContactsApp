package com.polyblack.contactsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.polyblack.contactsapp.databinding.ActivityMainBinding
import com.polyblack.contactsapp.ui.ContactDetailsFragment
import com.polyblack.contactsapp.ui.ContactListFragment
import com.polyblack.contactsapp.ui.ToolbarBackButtonListener

class MainActivity :
    AppCompatActivity(),
    ContactListFragment.OnContactSelectedListener,
    ToolbarBackButtonListener {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            addContactListFragment()
        }
    }

    override fun onContactSelected(id: Int) {
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
}
