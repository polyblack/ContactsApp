package com.polyblack.contactsapp.presentation.ui.fragments.contact_details

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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.polyblack.contactsapp.ContactsApplication
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.databinding.FragmentContactDetailsBinding
import com.polyblack.contactsapp.di.contact_details.ContactDetailsComponent
import com.polyblack.contactsapp.di.contact_details.ContactDetailsModule
import com.polyblack.contactsapp.presentation.presenters.contact_details.ContactDetailsViewModel
import com.polyblack.domain.entities.Contact
import javax.inject.Inject
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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ContactDetailsViewModel by viewModels { viewModelFactory }
    private var _binding: FragmentContactDetailsBinding? = null
    private val binding get() = _binding!!
    private var isPermissionGranted: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        if (!oldValue && newValue) {
            requestContact()
        }
    }
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component: ContactDetailsComponent? =
            (requireActivity().application as ContactsApplication).getAppComponent()
                ?.plusContactDetailsComponent(ContactDetailsModule())
        component?.inject(this)
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
            binding.progressBar.progressBarRecyclerView.isVisible = it.isLoading
            if (it.error != null) {
                Log.d("fragment_details", it.toString())
            } else {
                it.data?.let { contact -> onGetContactByIdResult(contact) }
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

    private fun onGetContactByIdResult(contact: Contact) {
        Log.d("fragment_details", "${contact.name} id= ${contact.id}")
        binding.birthdayNotificationSwitch.isEnabled = contact.isNotificationOn != null
        binding.birthdayNotificationSwitch.isChecked =
            (contact.isNotificationOn != false) && (contact.isNotificationOn != null)
        binding.birthdayNotificationSwitch.setOnCheckedChangeListener { _, _ ->
            viewModel.getContactWithNewNotificationStatus(contact)
        }
        binding.contactDetailsNameText.text = contact.name
        binding.contactDetailsNumber1Text.text = contact.number
        binding.contactDetailsNumber2Text.text = contact.number2
        binding.contactDetailsEmail1Text.text = contact.email
        binding.contactDetailsEmail2Text.text = contact.email2
        binding.contactDetailsAvatarImage.setImageURI(contact.avatarUri?.toUri())
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
