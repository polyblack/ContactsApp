package com.polyblack.contactsapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.databinding.FragmentContactListBinding
import com.polyblack.contactsapp.model.Contact

class ContactListFragment : Fragment(),
    OnContactsListResultListener {
    private var contactListener: OnContactSelectedListener? = null
    private var toolbarBackButtonListener: ToolbarBackButtonListener? = null
    private var serviceCommandsListener: ServiceCommandsListener? = null
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!
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
        if (context is ServiceCommandsListener) {
            serviceCommandsListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceCommandsListener?.getContactList(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.contactListAvatarImage.setOnClickListener { contactListener?.onContactSelected(1) }
        return view
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

    override fun onGetContactList(contactList: List<Contact>) {
        //TODO recyclerView и заполнение
    }
}
