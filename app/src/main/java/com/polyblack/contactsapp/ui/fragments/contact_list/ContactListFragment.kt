package com.polyblack.contactsapp.ui.fragments.contact_list

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
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
        newValue?.let {
            isServiceBound = true
            requestContactList()
        }
    }
    private var contactListReceiver: BroadcastReceiver? = null
    private var isServiceBound = false
    private var isPermissionGranted = false
    private val ACTION_CONTACT_LIST = "GET_CONTACT_LIST"
    private val NAME_CONTACT_LIST = "CONTACT_LIST"
    private val PERMISSION_REQUEST_CODE = 1

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentContactListBinding.inflate(inflater, container, false)
        .apply { _binding = this }.root

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
        if (isPermissionGranted && isServiceBound) {
            contactsService?.getContactList(
                requireContext()
            )
        }
    }

    private fun onGetContactListResult(contactList: ArrayList<Contact>) {
        binding.contactListAvatarImage.setOnClickListener {
            contactListener?.onContactSelected(contactList[0].id)
        }
        binding.contactListNameText.text = contactList[0].name
        binding.contactListNumberText.text = contactList[0].number
        binding.contactListAvatarImage.setImageURI(contactList[0].avatarUri?.toUri())
        for (contact in contactList) {
            Log.d("fragment_list", "${contact.name}  id= ${contact.id}")
        }
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                isPermissionGranted = true
                requestContactList()
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
                    requestContactList()
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
