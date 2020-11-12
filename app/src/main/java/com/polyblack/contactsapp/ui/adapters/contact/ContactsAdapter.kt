package com.polyblack.contactsapp.ui.adapters.contact

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.data.model.ContactListItem.Item
import com.polyblack.contactsapp.ui.adapters.contact.delegates.ContactListAdapterDelegate
import com.polyblack.contactsapp.ui.adapters.contact.delegates.ProgressBarAdapterDelegate

class ContactsAdapter(
    onContactListener: (Item) -> Unit
) : AsyncListDifferDelegationAdapter<ContactListItem>(ContactDiffUtil()) {
    init {
        delegatesManager.addDelegate(ContactListAdapterDelegate(fun(it: Int) {
            onContactListener(items[it] as Item)
        }))
        delegatesManager.addDelegate(ProgressBarAdapterDelegate())
    }

    class ContactDiffUtil<ContactListItem> : DiffUtil.ItemCallback<ContactListItem>() {
        override fun areItemsTheSame(oldItem: ContactListItem, newItem: ContactListItem): Boolean {
            return if (oldItem is Item && newItem is Item) {
                oldItem.contact.id == newItem.contact.id
            } else true
        }

        override fun areContentsTheSame(
            oldItem: ContactListItem,
            newItem: ContactListItem
        ): Boolean {
            return if (oldItem is Item && newItem is Item) {
                oldItem.contact.number == newItem.contact.number && oldItem.contact.avatarUri == newItem.contact.avatarUri
                        && oldItem.contact.name == newItem.contact.name
            } else true
        }
    }
}
