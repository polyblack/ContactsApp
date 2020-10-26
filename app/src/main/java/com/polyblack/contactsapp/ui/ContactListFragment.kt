package com.polyblack.contactsapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.polyblack.contactsapp.R
import com.polyblack.contactsapp.model.Contact

class ContactListFragment : Fragment() {
    private var contactListener: OnContactSelectedListener? = null
    private var toolbarBackButtonListener: ToolbarBackButtonListener? = null
    interface OnContactSelectedListener {
        fun onContactSelected(id: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnContactSelectedListener) {
            contactListener = context
        }
        if (context is ToolbarBackButtonListener) {
            toolbarBackButtonListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        val avatarImage = view.findViewById<ImageView>(R.id.contactListAvatarImage)
        avatarImage.setOnClickListener { contactListener?.onContactSelected(1) }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.title = getString(R.string.contacts)
        toolbarBackButtonListener?.setButtonVisibility(false)
    }

}