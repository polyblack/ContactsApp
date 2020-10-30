package com.polyblack.contactsapp.ui.fragments.contact_list

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.databinding.FragmentContactListBinding
import com.polyblack.contactsapp.model.Contact
import com.polyblack.contactsapp.service.ContactsService
import com.polyblack.contactsapp.ui.ServiceIBinderDepend
import kotlin.properties.Delegates

class ContactListFragment : Fragment(),
    ServiceIBinderDepend {
    private var contactListener: OnContactSelectedListener? = null
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private var contactsService: ContactsService? by Delegates.observable(null) { _, _, newValue ->
        newValue?.let { requestContactList() }
    }
    private var contactListReceiver: BroadcastReceiver? = null
    val ACTION_CONTACT_LIST = "GET_CONTACT_LIST"
    val NAME_CONTACT_LIST = "CONTACT_LIST"

    interface OnContactSelectedListener {
        fun onContactSelected(contactId: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnContactSelectedListener) {
            contactListener = context
        }
        contactListReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action.equals("GET_CONTACT_LIST")) {
                    onGetContactListResult(
                        intent?.getParcelableArrayListExtra<Contact>(
                            NAME_CONTACT_LIST
                        ) as ArrayList<Contact>
                    )
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentContactListBinding.inflate(inflater, container, false)
        .apply { _binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactListAvatarImage.setOnClickListener {
            contactListener?.onContactSelected(0)
        }
    }

    override fun onStart() {
        super.onStart()
        activity?.registerReceiver(contactListReceiver, IntentFilter(ACTION_CONTACT_LIST))
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.contacts)
        if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(contactListReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        contactListener = null
        super.onDestroy()
    }

    override fun setServiceBinder(service: IBinder) {
        val binder = service as ContactsService.ContactsBinder?
        contactsService = binder?.getService()
    }

    private fun requestContactList() {
        contactsService?.getContactList()
    }

    private fun onGetContactListResult(contactList: ArrayList<Contact>) {
        for (contact in contactList) {
            Log.d("fragment_contact_list", contact.name)
        }
    }
}
