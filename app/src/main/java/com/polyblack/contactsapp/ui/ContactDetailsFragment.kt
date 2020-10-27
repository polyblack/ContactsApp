package com.polyblack.contactsapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.polyblack.contactsapp.R

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

    private var contactId: Int? = null
    private var toolbarBackButtonListener: ToolbarBackButtonListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ToolbarBackButtonListener) {
            toolbarBackButtonListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getInt(ARG_CONTACT_ID)
        }
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
}
