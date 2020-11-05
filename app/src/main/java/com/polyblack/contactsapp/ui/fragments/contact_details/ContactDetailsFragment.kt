package com.polyblack.contactsapp.ui.fragments.contact_details

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.data.model.Contact
import com.polyblack.contactsapp.databinding.FragmentContactDetailsBinding
import com.polyblack.contactsapp.ui.activity.MainActivity
import com.polyblack.contactsapp.utils.DateUtils.Companion.getTimeLeftInMillis
import kotlin.properties.Delegates

private const val ARG_CONTACT_ID = "contactId"

class ContactDetailsFragment : Fragment() {
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
    private val viewModel: ContactDetailsViewModel by viewModels()
    private var isPermissionGranted: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        if (!oldValue && newValue) {
            requestContact()
        }
    }
    private val ACTION_NOTIFICATION = "CONTACT_BIRTHDAY_NOTIFICATION"
    private val EXTRA_NOTIFICATION = "NOTIFICATION_MESSAGE"
    private val EXTRA_CONTACT_ID = "CONTACT_ID"
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isPermissionGranted) {
            requestContact()
        } else {
            checkPermission()
        }
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
        viewModel.contact.observe(viewLifecycleOwner, {
            onGetContactByIdResult(it)
        })
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
        activity?.title = getString(R.string.profile)
        if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }

    private fun requestContact() {
        if (isPermissionGranted) {
            arguments?.getInt(ARG_CONTACT_ID)?.let { viewModel.getContact(it) }
        }
    }

    private fun onGetContactByIdResult(contact: Contact) {
        Log.d("fragment_details", "${contact.name} id= ${contact.id}")
        binding.birthdayNotificationSwitch.visibility = View.VISIBLE
        binding.birthdayNotificationSwitch.isChecked = checkIfNotificationIsEnabled(contact)
        binding.birthdayNotificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            changeNotificationState(isChecked, contact)
        }
        binding.contactDetailsNameText.text = contact.name
        binding.contactDetailsNumber1Text.text = contact.number
        binding.contactDetailsNumber2Text.text = contact.number2
        binding.contactDetailsEmail1Text.text = contact.email
        binding.contactDetailsEmail2Text.text = contact.email2
        binding.contactDetailsAvatarImage.setImageURI(contact.avatarUri?.toUri())
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
                System.currentTimeMillis() + getTimeLeftInMillis(contact.birthday),
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

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                isPermissionGranted = true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_rationale),
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    isPermissionGranted = true
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permission_denied),
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}
