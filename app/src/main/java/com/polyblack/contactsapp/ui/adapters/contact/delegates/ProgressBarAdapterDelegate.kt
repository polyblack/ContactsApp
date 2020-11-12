package com.polyblack.contactsapp.ui.adapters.contact.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.databinding.ProgressbarRecyclerviewBinding

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
            ProgressbarRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    class ProgressBarViewHolder(binding: ProgressbarRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}
