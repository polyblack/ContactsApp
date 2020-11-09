package com.polyblack.contactsapp.ui.adapters.contact.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.polyblack.contactsapp.data.model.ContactListItem
import com.polyblack.contactsapp.databinding.ItemRecyclerviewContactlistBinding

class ContactListAdapterDelegate(private val onContactListener: (Int) -> Unit) :
    AbsListItemAdapterDelegate<ContactListItem.Item, ContactListItem, ContactListAdapterDelegate.ContactsViewHolder>() {

    override fun isForViewType(
        item: ContactListItem,
        items: List<ContactListItem>,
        position: Int
    ) = item is ContactListItem.Item

    override fun onCreateViewHolder(parent: ViewGroup): ContactsViewHolder =
        ContactsViewHolder(
            ItemRecyclerviewContactlistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onContactListener
        )

    override fun onBindViewHolder(
        item: ContactListItem.Item,
        viewHolder: ContactsViewHolder,
        payloads: List<Any>
    ) = viewHolder.bind(item)


    class ContactsViewHolder(
        binding: ItemRecyclerviewContactlistBinding,
        onContactListener: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val binding = ItemRecyclerviewContactlistBinding.bind(itemView)

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onContactListener.invoke(bindingAdapterPosition)
                }
            }
        }

        fun bind(contactItem: ContactListItem.Item) {
            binding.contactListNameText.text = contactItem.contact.name
            binding.contactListNumberText.text = contactItem.contact.number
            binding.contactListAvatarImage.setImageURI(contactItem.contact.avatarUri?.toUri())
        }
    }
}
