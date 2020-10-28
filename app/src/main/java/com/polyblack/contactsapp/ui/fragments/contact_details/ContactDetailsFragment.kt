package com.polyblack.contactsapp.ui.fragments.contact_details

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
import com.polyblack.contactsapp.model.Contact
import com.polyblack.contactsapp.service.ContactsService
import com.polyblack.contactsapp.ui.ServiceIBinderDepend
import com.polyblack.contactsapp.ui.activity.ToolbarBackButtonListener
import kotlin.properties.Delegates

private const val ARG_CONTACT_ID = "contactId"

class ContactDetailsFragment : Fragment(),
    ServiceIBinderDepend {
    companion object {
        @JvmStatic
        fun newInstance(contactId: Int) =
            ContactDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_CONTACT_ID, contactId)
                }
            }
    }

    private var toolbarBackButtonListener: ToolbarBackButtonListener? = null
    private var contactsService: ContactsService? by Delegates.observable(null) { _, _, newValue ->
        newValue?.let { requestContactById() }
    }
    private lateinit var contactReceiver: BroadcastReceiver
    val ACTION_CONTACT = "GET_CONTACT"
    val NAME_CONTACT = "CONTACT"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ToolbarBackButtonListener) {
            toolbarBackButtonListener = context
        }
        contactReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action.equals(ACTION_CONTACT)) {
                    intent?.getParcelableExtra<Contact>(NAME_CONTACT)
                        ?.let { onGetContactByIdResult(it) }
                }
            }
        }
        activity?.registerReceiver(contactReceiver, IntentFilter(ACTION_CONTACT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_contact_details, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.profile)
        toolbarBackButtonListener?.setButtonVisibility(true)
    }

    override fun onDetach() {
        activity?.unregisterReceiver(contactReceiver)
        super.onDetach()
    }

    override fun onDestroy() {
        toolbarBackButtonListener = null
        super.onDestroy()
    }

    fun onGetContactByIdResult(contact: Contact) {
        Log.d("fragment_details", contact.name)
    }

    override fun setServiceBinder(service: IBinder) {
        val binder = service as ContactsService.ContactsBinder
        contactsService = binder.getService()
    }

    private fun requestContactById() {
        arguments?.getInt(ARG_CONTACT_ID)?.let { contactsService?.getContactById(it) }
    }
}
