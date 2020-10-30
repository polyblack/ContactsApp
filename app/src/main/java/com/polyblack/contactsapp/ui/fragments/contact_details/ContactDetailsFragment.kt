package com.polyblack.contactsapp.ui.fragments.contact_details

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
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
import com.polyblack.contactsapp.databinding.FragmentContactDetailsBinding
import com.polyblack.contactsapp.model.Contact
import com.polyblack.contactsapp.service.ContactsService
import com.polyblack.contactsapp.ui.ServiceIBinderDepend
import com.polyblack.contactsapp.ui.activity.MainActivity
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

    private var _binding: FragmentContactDetailsBinding? = null
    private val binding get() = _binding!!
    private var contactsService: ContactsService? by Delegates.observable(null) { _, _, newValue ->
        newValue?.let { requestContactById() }
    }
    private lateinit var contactReceiver: BroadcastReceiver
    private val ACTION_CONTACT = "GET_CONTACT"
    private val NAME_CONTACT = "CONTACT"
    private val ACTION_NOTIFICATION = "CONTACT_BIRTHDAY_NOTIFICATION"
    private val EXTRA_NOTIFICATION = "NOTIFICATION_MESSAGE"
    private val EXTRA_CONTACT_ID = "CONTACT_ID"

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
    ): View? = FragmentContactDetailsBinding.inflate(inflater, container, false)
        .apply { _binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.birthdayNotificationSwitch.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        activity?.registerReceiver(contactReceiver, IntentFilter(ACTION_CONTACT))
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.profile)
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
        activity?.unregisterReceiver(contactReceiver)
    }

    override fun setServiceBinder(service: IBinder) {
        val binder = service as ContactsService.ContactsBinder
        contactsService = binder.getService()
    }

    private fun onGetContactByIdResult(contact: Contact) {
        Log.d("fragment_details", contact.name)
        binding.birthdayNotificationSwitch.visibility = View.VISIBLE
        binding.birthdayNotificationSwitch.isChecked = checkIfNotificationIsEnabled(contact)
        binding.birthdayNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            changeNotificationState(isChecked, contact)
        }
    }

    private fun requestContactById() {
        arguments?.getInt(ARG_CONTACT_ID)?.let { contactsService?.getContactById(it) }
    }

    private fun changeNotificationState(isSwitchOn: Boolean, contact: Contact) {
        val alarmPendingIntent = createNotificationIntent(contact).let {
            PendingIntent.getBroadcast(context, 0, it, FLAG_CANCEL_CURRENT)
        }
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (isSwitchOn) {
            (activity as MainActivity).createNotificationChannel()
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 4000,
                alarmPendingIntent
            )
        } else {
            if (checkIfNotificationIsEnabled(contact)) {
                alarmManager.cancel(alarmPendingIntent)
                alarmPendingIntent.cancel()
            }
        }
    }

    private fun checkIfNotificationIsEnabled(contact: Contact): Boolean =
        PendingIntent.getBroadcast(
            context,
            0,
            createNotificationIntent(contact),
            PendingIntent.FLAG_NO_CREATE
        ) != null

    private fun createNotificationIntent(contact: Contact): Intent =
        Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_NOTIFICATION
            putExtra(EXTRA_CONTACT_ID, arguments?.getInt(ARG_CONTACT_ID))
            putExtra(
                EXTRA_NOTIFICATION,
                getString(R.string.notification_message) + " ${contact.name}"
            )
        }
}
