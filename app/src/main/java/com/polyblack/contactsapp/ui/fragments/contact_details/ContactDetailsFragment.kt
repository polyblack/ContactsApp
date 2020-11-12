package com.polyblack.contactsapp.ui.fragments.contact_details

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.databinding.FragmentContactDetailsBinding
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
        viewModel.contact.observe(viewLifecycleOwner, {
            when (it.data) {
                is ContactListItem.Loading ->
                    binding.progressBar.progressBarRecyclerView.visibility = View.VISIBLE
                is ContactListItem.Error -> {
                    binding.progressBar.progressBarRecyclerView.visibility = View.INVISIBLE
                    Log.d("fragment_details", it.toString())
                }
                is ContactListItem.Item -> {
                    binding.progressBar.progressBarRecyclerView.visibility = View.INVISIBLE
                    onGetContactByIdResult(it.data)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
        activity?.title = getString(R.string.profile)
    }

    private fun requestContact() {
        if (isPermissionGranted) {
            arguments?.getInt(ARG_CONTACT_ID)?.let { viewModel.getContact(it) }
        }
    }

    private fun onGetContactByIdResult(contactItem: ContactListItem.Item) {
        Log.d("fragment_details", "${contactItem.contact.name} id= ${contactItem.contact.id}")
        binding.birthdayNotificationSwitch.isEnabled = contactItem.contact.isNotificationOn != null
        binding.birthdayNotificationSwitch.isChecked =
            (contactItem.contact.isNotificationOn != false) && (contactItem.contact.isNotificationOn != null)
        binding.birthdayNotificationSwitch.setOnCheckedChangeListener { _, _ ->
            viewModel.changeContactNotificationStatus(contactItem)
        }
        binding.contactDetailsNameText.text = contactItem.contact.name
        binding.contactDetailsNumber1Text.text = contactItem.contact.number
        binding.contactDetailsNumber2Text.text = contactItem.contact.number2
        binding.contactDetailsEmail1Text.text = contactItem.contact.email
        binding.contactDetailsEmail2Text.text = contactItem.contact.email2
        binding.contactDetailsAvatarImage.setImageURI(contactItem.contact.avatarUri?.toUri())
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
