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
import androidx.fragment.app.Fragment
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.databinding.FragmentContactListBinding
import com.polyblack.contactsapp.model.Contact
import com.polyblack.contactsapp.service.ContactsService
import com.polyblack.contactsapp.ui.ServiceIBinderDepend
import com.polyblack.contactsapp.ui.activity.ToolbarBackButtonListener
import kotlin.properties.Delegates

class ContactListFragment : Fragment(),
    ServiceIBinderDepend {
    private var contactListener: OnContactSelectedListener? = null
    private var toolbarBackButtonListener: ToolbarBackButtonListener? = null
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private var contactsService: ContactsService? by Delegates.observable(null) { _, _, newValue ->
        newValue?.let { requestContactList() }
    }
    private val contactListReceiver: BroadcastReceiver? = null
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
        if (context is ToolbarBackButtonListener) {
            toolbarBackButtonListener = context
        }
        val contactListReceiver = object : BroadcastReceiver() {
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
        context.registerReceiver(contactListReceiver, IntentFilter(ACTION_CONTACT_LIST))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentContactListBinding.inflate(inflater, container, false).apply { _binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contactListAvatarImage.setOnClickListener {
            contactListener?.onContactSelected(0)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.contacts)
        toolbarBackButtonListener?.setButtonVisibility(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        contactListener = null
        toolbarBackButtonListener = null
        activity?.unregisterReceiver(contactListReceiver)
        super.onDestroy()
    }

    private fun requestContactList() {
        contactsService?.getContactList()
    }

    fun onGetContactListResult(contactList: ArrayList<Contact>) {
        for (contact in contactList) {
            Log.d("fragment_contact_list", contact.name)
        }
    }

    override fun setServiceBinder(service: IBinder) {
        val binder = service as ContactsService.ContactsBinder?
        contactsService = binder?.getService()
    }
}
