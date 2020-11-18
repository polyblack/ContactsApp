package com.polyblack.contactsapp.presentation.ui.adapters.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.polyblack.contactsapp.databinding.ProgressbarBinding
import com.polyblack.domain.entities.ContactListItem

class ProgressBarAdapterDelegate :
    AbsListItemAdapterDelegate<ContactListItem.Loading, ContactListItem, ProgressBarAdapterDelegate.ProgressBarViewHolder>() {

    override fun isForViewType(
        item: ContactListItem,
        items: List<ContactListItem>,
        position: Int
    ) = item is ContactListItem.Loading


    override fun onBindViewHolder(
        item: ContactListItem.Loading,
        holder: ProgressBarViewHolder,
        payloads: List<Any>
    ) = Unit

    override fun onCreateViewHolder(parent: ViewGroup) =
        ProgressBarViewHolder(
            ProgressbarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    class ProgressBarViewHolder(binding: ProgressbarBinding) :
        RecyclerView.ViewHolder(binding.root)
}
