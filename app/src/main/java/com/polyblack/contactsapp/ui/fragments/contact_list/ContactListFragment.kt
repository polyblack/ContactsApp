package com.polyblack.contactsapp.ui.fragments.contact_list

import android.Manifest
import android.content.Context
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
import com.polyblack.contactsapp.databinding.FragmentContactListBinding
import kotlin.properties.Delegates

class ContactListFragment : Fragment() {
    private var contactListener: OnContactSelectedListener? = null
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactListViewModel by viewModels()
    private var isPermissionGranted: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        if (!oldValue && newValue) {
            requestContactList()
        }
    }
    private val PERMISSION_REQUEST_CODE = 1

    interface OnContactSelectedListener {
        fun onContactSelected(contactId: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnContactSelectedListener) {
            contactListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isPermissionGranted) {
            requestContactList()
        } else {
            checkPermission()
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
        viewModel.contactList.observe(viewLifecycleOwner, { onGetContactListResult(it) })
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
        activity?.title = getString(R.string.contacts)
        if (activity?.supportFragmentManager?.backStackEntryCount!! > 0) {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayShowHomeEnabled(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        contactListener = null
        super.onDestroy()
    }

    private fun requestContactList() {
        if (isPermissionGranted) {
            viewModel.getContacts()
        }
    }

    private fun onGetContactListResult(contactList: List<Contact>) {
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
