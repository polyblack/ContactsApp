package com.polyblack.contactsapp.ui.fragments.contact_list

import android.Manifest
import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.databinding.FragmentContactListBinding
import com.polyblack.contactsapp.ui.adapters.contact.ContactsAdapter
import kotlin.properties.Delegates

class ContactListFragment : Fragment() {
    private var contactListener: OnContactSelectedListener? = null
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactListViewModel by viewModels()
    private var contactsAdapter: ContactsAdapter? = null
    private var isPermissionGranted: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        if (!oldValue && newValue) {
            requestContactList()
        }
    }
    private val PERMISSION_REQUEST_CODE = 1

    interface OnContactSelectedListener {
        fun onContactSelected(contactItem: ContactListItem.Item)
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
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentContactListBinding.inflate(inflater, container, false)
        .apply { _binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.contactList.observe(viewLifecycleOwner, {
            contactsAdapter?.items = it.data
        })
        contactsAdapter = ContactsAdapter(::onContactClick)
        with(binding.contactListRecyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
        activity?.title = getString(R.string.contacts)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menu.findItem(R.id.searchView).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(typing: String?): Boolean =
                viewModel.showContactListByTyping(typing)

            override fun onQueryTextChange(typing: String?): Boolean =
                viewModel.showContactListByTyping(typing)
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        contactsAdapter = null
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

    private fun onGetContactListResult(contactItemList: List<ContactListItem>) {
        contactsAdapter?.items = contactItemList
        for (contactItem in contactItemList) {
            if (contactItem is ContactListItem.Item) {
                Log.d("fragment_list", "${contactItem.contact.name}  id= ${contactItem.contact.id}")
            }
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

    private fun onContactClick(contactItem: ContactListItem.Item) {
        contactListener?.onContactSelected(contactItem)
    }
}
